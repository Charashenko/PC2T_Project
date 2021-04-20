package project.strategies.db;

import project.*;
import project.db.Database;

import java.util.List;
import java.util.Scanner;

public class Read_Person_from_DB implements IStrategy {

    private Database db;

    public Read_Person_from_DB(Database db) {
        this.db = db;
    }

    @Override
    public StrategyResult execute(List<Person> peopleRamDB) throws CustomException {
        if (db.getConnection() == null) { // check connection state
            return new StrategyResult(false, "You are not connected to the database");
        }

        // overwrite safety
        Scanner sc = new Scanner(System.in);
        System.out.println("[Warning] This action will overwrite current person in memory, continue? [y/n]");
        boolean end = false;
        String line;
        while (!end) {
            line = sc.nextLine();
            switch (line) {
                case "y":
                    end = true;
                    break;
                case "n":
                    return new StrategyResult(true, "Action was cancelled");
                default:
                    System.out.println("[Warning] Invalid choice [y/n]");
                    break;
            }
        }

        System.out.println("[Info] Select person's ID from DB to be read");
        List<Person> peopleSQLDB = db.getPeople();
        for (Person p : peopleSQLDB) {
            System.out.format("\t[%d] %s%n", p.getID(), p.getFullName());
        }

        int id = University.onlyInt(0, 10000);
        Person sqlPerson = getPerson(peopleSQLDB, id);
        if (sqlPerson != null) {
            for (Person p : peopleRamDB){
                if(p.getID() == sqlPerson.getID()){
                    if(p instanceof Teacher){
                        Teacher t = (Teacher) p;
                        t.setName(sqlPerson.getName());
                        t.setSurname(sqlPerson.getSurname());
                        t.setBirthDate(sqlPerson.getBirthDate());
                        t.setSalary(sqlPerson.getSalary());
                        t.setStudents(((Teacher) sqlPerson).getStudents());
                    } else {
                        Student s = (Student) p;
                        s.setName(sqlPerson.getName());
                        s.setSurname(sqlPerson.getSurname());
                        s.setBirthDate(sqlPerson.getBirthDate());
                        s.setSalary(sqlPerson.getSalary());
                        s.setTeachers(((Student) sqlPerson).getTeachers());
                        s.setAvg(((Student) sqlPerson).getAvg());
                        s.setGrades(((Student) sqlPerson).getGrades());
                    }
                    return new StrategyResult(true, "Person was read from DB");
                }
            }
            peopleRamDB.add(sqlPerson);
            return new StrategyResult(true, "Person was read from DB");
        } else {
            return new StrategyResult(false, "Person with that ID doesn't exist");
        }
    }

    public Person getPerson(List<Person> people, int id) {
        for (Person p : people) if (p.getID() == id) return p;
        return null;
    }
}
