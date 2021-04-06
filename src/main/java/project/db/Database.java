package project.db;

import project.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Database {

    private Connection connection = null;
    private boolean isAccessible = false;

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
                    connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/project/db/university.db");
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return connection;
        }
        return null;
    }

    public void printConnection() { // print DB connection
        try {
            System.out.println(getConnection().getSchema());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() { // close connection to DB
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public ResultSet queryDB(String query) { // universal query
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

    public List<Person> getPeople() { // get all people from DB
        List<Person> people = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        try {
            // get all teachers from db
            ResultSet rs = queryDB("SELECT * FROM teachers");
            while (rs.next()) {
                Teacher t = new Teacher(rs.getInt("ID"), rs.getString("name"),
                        rs.getString("surname"), rs.getString("birthdate"), rs.getDouble("salary"));
                teachers.add(t);
            }
            people.addAll(teachers);

            // get all students of individual teachers
            for (Teacher t : teachers) {
                // get IDs of teacher's students
                rs = queryDB("SELECT * FROM teacher_students WHERE teacher_id=" + t.getID());
                List<Integer> ids = new ArrayList<>();
                while (rs.next()) {
                    ids.add(rs.getInt("student_id"));
                }

                // add students to teacher
                for (int i : ids) {
                    // get student info
                    rs = queryDB("SELECT * FROM students WHERE ID=" + i);
                    Student s = new Student(rs.getString("name"), rs.getString("surname"),
                            rs.getString("birthdate"), t);
                    s.setID(rs.getInt("ID"));
                    s.setSalary(rs.getDouble("salary"));

                    // get student's grades
                    rs = queryDB("SELECT * FROM student_grades WHERE student_id=" + i);
                    while (rs.next()) {
                        s.addGrade(new Grade(rs.getString("subject"), rs.getInt("grade")));
                    }

                    // get student's teachers
                    rs = queryDB("SELECT * FROM student_teachers WHERE student_id=" + i);
                    while (rs.next()) {
                        ResultSet finalRs = rs;
                        List<Teacher> studentTeachers = teachers.stream().filter(o -> { // filter teachers based on ID
                            try {
                                return o.getID() == finalRs.getInt("teacher_id");
                            } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                return false;
                            }
                        }).collect(Collectors.toList());
                        for (Teacher studentTeacher : studentTeachers) {
                            s.addTeacher(studentTeacher);
                        }
                    }

                    // add student to teacher
                    t.addStudent(s);
                    if (!students.contains(s)) {
                        students.add(s);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        people.addAll(students);
        return people; // return list of all people in DB
    }

    public void insertTeacher(Teacher t) { // insert teacher into DB
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO teachers (ID,name,surname,birthdate,salary) VALUES (?,?,?,?,?)");
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

    public void insertStudent(Student s) { // insert student into DB
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO students (ID,name,surname,birthdate,salary,studyaverage) VALUES" +
                    "(?,?,?,?,?,?)");
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

    public void clearTables() { // remove everything from tables
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

    public void removeTeacher(int id) { // remove teacher from DB

    }

}
