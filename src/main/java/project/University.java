package project;

import org.junit.Test;
import project.db.Database;
import project.strategies.*;
import project.strategies.db.*;

import java.util.*;

import static junit.framework.Assert.assertNotNull;

public class University {

    private static List<Person> people = new ArrayList<>();
    private static final Database sqlDB = new Database();

    public static int onlyInt(int start, int end) {
        Scanner sc = new Scanner(System.in);
        try {
            int i = sc.nextInt();
            if (i > end || i < start)
                throw new CustomException("[Warning] Specified number is out of range");
            return i;
        } catch (InputMismatchException e) {
            System.out.println("[Warning] Only integers are accepted");
            return onlyInt(start, end);
        } catch (CustomException e) {
            System.out.format("%s (%d - %d)%n", e.getMessage(), start, end);
            return onlyInt(start, end);
        }
    }

    public static void main(String[] args) {
        //TEST
        people.add(new Teacher("Miro", "Vojnik", "22.03.2000"));
        people.add(new Teacher("Ladislav", "Kobylko", "20.03.1990"));
        people.add(new Student("Jozo", "Marchev", "16.04.2001", (Teacher) people.get(0)));
        people.add(new Student("Ondrej", "Palpati", "10.03.1999", (Teacher) people.get(0)));
        ((Teacher) people.get(1)).addStudent((Student) people.get(2));
        ((Student) people.get(2)).addGrade(new Grade("math", 1));
        ((Student) people.get(2)).addGrade(new Grade("anj", 5));
        ((Student) people.get(2)).addGrade(new Grade("chem", 5));
        ((Student) people.get(3)).addGrade(new Grade("math", 2));
        ((Student) people.get(3)).addGrade(new Grade("anj", 3));
        ((Student) people.get(3)).addGrade(new Grade("chem", 3));
        //TEST END
        boolean end = false;
        int option;
        Strategy stc = null;
        Scanner sc = new Scanner(System.in);
        while (!end) {
            System.out.println("\n\t---- [OPTIONS] ----");
            System.out.println("[1] Add new person to university");
            System.out.println("[2] Set new grade for student");
            System.out.println("[3] Remove person from university");
            System.out.println("[4] Print all teachers of student");
            System.out.println("[5] Add or remove teacher's student");
            System.out.println("[6] Print person info");
            System.out.println("[7] Print teachers sorted by student count");
            System.out.println("[8] Print teacher's students sorted by study average");
            System.out.println("[9] Print people sorted by surname");
            System.out.println("[10] Get total university expenses");
            System.out.println("[11] Connect to SQL database");
            System.out.println("[12] Read all data from SQL database");
            System.out.println("[13] Save all data to SQL database");
            System.out.println("[14] Delete person from SQL database");
            System.out.println("[15] Read person from SQL database");
            System.out.println("[16] Print all people and their info");
            System.out.println("[17] Change person ID (first scan old ID then new ID)");
            System.out.println("[0] End application");
            option = onlyInt(0, 17);
            try {
                switch (option) {
                    case 1:
                        stc = new Strategy(new Add_Person_To_University());
                        break;
                    case 2:
                        stc = new Strategy(new Set_New_Grade());
                        break;
                    case 3:
                        stc = new Strategy(new Rem_Person_From_University());
                        break;
                    case 4:
                        stc = new Strategy(new Print_Teachers_Of_Student());
                        break;
                    case 5:
                        stc = new Strategy(new Assignment_Of_Students_To_Teacher());
                        break;
                    case 6:
                        stc = new Strategy(new Print_Person_Info());
                        break;
                    case 7:
                        stc = new Strategy(new Print_Teachers_Based_On_Student_Count());
                        break;
                    case 8:
                        stc = new Strategy(new Print_Teacher_Students_Based_On_Study_Average());
                        break;
                    case 9:
                        stc = new Strategy(new Print_People_Sorted_By_Surname());
                        break;
                    case 10:
                        stc = new Strategy(new Get_University_Expenses());
                        break;
                    case 11:
                        stc = new Strategy(new Connect_to_SQL_database(sqlDB));
                        break;
                    case 12:
                        stc = new Strategy(new Read_All_Data_from_DB(sqlDB));
                        break;
                    case 13:
                        stc = new Strategy(new Write_All_Data_to_DB(sqlDB));
                        break;
                    case 14:
                        stc = new Strategy(new Delete_Person_from_DB(sqlDB));
                        break;
                    case 15:
                        stc = new Strategy(new Read_Person_from_DB(sqlDB));
                        break;
                    case 16:
                        printPeople();
                        break;
                    case 17:
                        changePersonID(onlyInt(0, 10000), onlyInt(0, 10000));
                        break;
                    case 0:
                        end = true;
                        sqlDB.disconnect();
                        break;
                }
                if (stc != null) {
                    StrategyResult ans = stc.executeStrategy(people);
                    if (ans != null) {
                        if (ans.getResult()) {
                            if (!ans.getMessage().equals("Null")) {
                                System.out.println("[Info] " + ans.getMessage());
                            }
                        } else {
                            System.out.println("[Warning] " + ans.getMessage());
                        }
                    }
                    System.out.println("  Press enter to continue...");
                    sc.nextLine();
                    stc = null;
                }
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void printPeople() { // prints all people
        for (Person p : people) {
            if (p instanceof Teacher) p.printInfo();
            else if (p instanceof Student) p.printInfo();
        }
    }

    public static List<Teacher> getTeachers() { // returns all teachers
        List<Teacher> t = new ArrayList<>();
        for (Person p : people) if (p instanceof Teacher) t.add((Teacher) p);
        return t;
    }

    public static List<Student> getStudents() { // returns all students
        List<Student> s = new ArrayList<>();
        for (Person p : people) if (p instanceof Student) s.add((Student) p);
        return s;
    }

    public static Teacher getTeacher(int ID) { // returns teacher with specified id
        for (Person p : people) if (p.getID() == ID) return (Teacher) p;
        return null;
    }

    public static Student getStudent(int ID) { // returns student with specified id
        for (Person p : people) if (p.getID() == ID) return (Student) p;
        return null;
    }

    public static double getExpenses() { // returns total university expenses
        double out = 0;
        for (Person p : people) out += p.getSalary();
        return out;
    }

    public static Person getPerson(int ID) { // returns person with specified id
        for (Person p : people) if (p.getID() == ID) return p;
        return null;
    }

    public static void remPerson(int ID) { // removes person from university
        Person p = getPerson(ID);
        if (p == null) return;
        if (p instanceof Teacher) {
            for (Student s : ((Teacher) p).getStudents()) {
                ((Teacher) p).remStudent(s);
                s.remTeacher((Teacher) p);
            }
        } else {
            for (Teacher t : ((Student) p).getTeachers()) {
                ((Student) p).remTeacher(t);
                t.remStudent((Student) p);
            }
        }
        people.remove(p);
    }

    @Test
    public void testDatabaseConnection() { // tests database connection
        sqlDB.setAccessible(true);
        assertNotNull(sqlDB.getConnection());
        sqlDB.setAccessible(false);
    }

    public static void changePersonID(int oldID, int newID){
        Person p = getPerson(oldID);
        if (p != null) p.setID(newID);
    }
}
