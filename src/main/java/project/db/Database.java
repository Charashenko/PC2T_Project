package project.db;

import project.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Connection connection = null;
    private boolean isAccessible = false;
    private String path = "src/main/java/project/db/university.db"; // default path

    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public Connection getConnection() { // connect to DB
        if (isAccessible) {
            if (connection == null) {
                try {
                    Class.forName("org.sqlite.JDBC");
                    connection = DriverManager.getConnection("jdbc:sqlite:" + path);
                    createNewDB();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return connection;
        }
        return null;
    }

    /**
     * Prints DB connection
     */
    public void printConnection() {
        try {
            System.out.println(getConnection().getSchema());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets path to DB file
     * @param path Path to be set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Closes connection to DB
     */
    public void disconnect() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Universal query
     * @param query Query to be executed
     * @return Results of query
     */
    public ResultSet queryDB(String query) {
        if (getConnection() == null) {
            return null;
        }
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all people from DB
     * @return List of all people
     */
    public List<Person> getPeople() {
        List<Person> people = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        try {
            // get all teachers from db
            ResultSet rs = queryDB("SELECT * FROM teachers");
            while (rs.next()) {
                Teacher t = new Teacher(
                        rs.getInt("ID"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("birthdate"),
                        rs.getDouble("salary"));
                teachers.add(t);
            }
            people.addAll(teachers);

            // get all students from DB
            rs = queryDB("SELECT * FROM students");
            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("ID"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("birthdate"),
                        rs.getDouble("salary"),
                        rs.getDouble("studyaverage"));
                students.add(s);
            }
            people.addAll(students);

            // get all students of individual teachers
            for (Teacher t : teachers) {
                rs = queryDB("SELECT * FROM teacher_students WHERE teacher_id=" + t.getID());
                while (rs.next()) {
                    Person p = getPerson(rs.getInt("student_id"), people);
                    t.addStudent((Student) p);
                }
            }

            // get all teachers of individual students
            for (Student s : students) {
                rs = queryDB("SELECT * FROM student_teachers WHERE student_id=" + s.getID());
                while (rs.next()) {
                    Person p = getPerson(rs.getInt("teacher_id"), people);
                    s.addTeacher((Teacher) p);
                }
            }

            // get all grades of individual students
            for (Student s : students) {
                rs = queryDB("SELECT * FROM student_grades WHERE student_id=" + s.getID());
                while (rs.next()) {
                    s.addGrade(new Grade(rs.getString("subject"), rs.getInt("grade")));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return people; // return list of all people in DB
    }

    /**
     * Inserts teacher into DB
     * @param t Teacher to be inserted
     */
    public void insertTeacher(Teacher t) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "INSERT INTO teachers (ID,name,surname,birthdate,salary) VALUES (?,?,?,?,?)");
            ps.setInt(1, t.getID());
            ps.setString(2, t.getName());
            ps.setString(3, t.getSurname());
            ps.setString(4, t.getBirthDate());
            ps.setDouble(5, t.getSalary());
            ps.execute();
            for (Student s : t.getStudents()) {
                ps = getConnection().prepareStatement("INSERT INTO teacher_students (teacher_id,student_id) VALUES (?,?)");
                ps.setInt(1, t.getID());
                ps.setInt(2, s.getID());
                ps.execute();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *  Inserts student into DB
     * @param s Student to be inserted
     */
    public void insertStudent(Student s) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "INSERT INTO students (ID,name,surname,birthdate,salary,studyaverage) VALUES (?,?,?,?,?,?)");
            ps.setInt(1, s.getID());
            ps.setString(2, s.getName());
            ps.setString(3, s.getSurname());
            ps.setString(4, s.getBirthDate());
            ps.setDouble(5, s.getSalary());
            ps.setDouble(6, s.getAvg());
            ps.execute();

            for (Teacher t : s.getTeachers()) {
                ps = getConnection().prepareStatement("INSERT INTO student_teachers (student_id, teacher_id) VALUES (?,?)");
                ps.setInt(1, s.getID());
                ps.setInt(2, t.getID());
                ps.execute();
            }

            for (Grade g : s.getGrades()) {
                ps = getConnection().prepareStatement("INSERT INTO student_grades (student_id, subject, grade) VALUES" +
                        "(?,?,?)");
                ps.setInt(1, s.getID());
                ps.setString(2, g.getSubject());
                ps.setInt(3, g.getGrade());
                ps.execute();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates new SQL tables if they don't exist
     */
    public void createNewDB() {
        try {
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS students (" +
                    "ID int primary key not null," +
                    "name varchar(100) not null," +
                    "surname varchar(100) not null," +
                    "birthdate varchar(20) not null," +
                    "salary double default 0," +
                    "studyaverage double default 1)").execute();
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS teachers (" +
                    "ID int primary key not null," +
                    "name varchar(100) not null," +
                    "surname varchar(100) not null," +
                    "birthdate varchar(20) not null," +
                    "salary double default 0)").execute();
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS student_teachers (" +
                    "student_id int not null," +
                    "teacher_id int not null)").execute();
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS teacher_students (" +
                    "teacher_id int not null," +
                    "student_id int not null)").execute();
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS student_grades (" +
                    "student_id int not null," +
                    "subject varchar(20) not null," +
                    "grade int not null)").execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Purge everything from DB tables
     */
    public void clearTables() {
        try {
            getConnection().prepareStatement("DELETE FROM student_grades").execute();
            getConnection().prepareStatement("DELETE FROM student_teachers").execute();
            getConnection().prepareStatement("DELETE FROM teacher_students").execute();
            getConnection().prepareStatement("DELETE FROM teachers").execute();
            getConnection().prepareStatement("DELETE FROM students").execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes teacher from DB
     * @param id ID of teacher
     */
    public void removeTeacher(int id) {
        try {
            getConnection().prepareStatement("DELETE FROM teachers WHERE id=" + id).execute();
            getConnection().prepareStatement("DELETE FROM student_teachers WHERE teacher_id=" + id).execute();
            getConnection().prepareStatement("DELETE FROM teacher_students WHERE teacher_id=" + id).execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Removes student from DB
     * @param id ID of student
     */
    public void removeStudent(int id) {
        try {
            getConnection().prepareStatement("DELETE FROM students WHERE id=" + id).execute();
            getConnection().prepareStatement("DELETE FROM student_teachers WHERE student_id=" + id).execute();
            getConnection().prepareStatement("DELETE FROM teacher_students WHERE student_id=" + id).execute();
            getConnection().prepareStatement("DELETE FROM student_grades WHERE student_id=" + id).execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Gets person based on ID from list of people
     * @param id ID of wanted person
     * @param people List of people to search from
     * @return Found person
     */
    public Person getPerson(int id, List<Person> people) {
        for (Person p : people) if (p.getID() == id) return p;
        return null;
    }

}
