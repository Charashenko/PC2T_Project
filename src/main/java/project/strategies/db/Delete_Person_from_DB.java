package project.strategies.db;

import project.*;
import project.db.Database;

import java.util.List;

public class Delete_Person_from_DB implements IStrategy {

    private final Database db;

    public Delete_Person_from_DB(Database db) {
        this.db = db;
    }

    @Override
    public StrategyResult execute(List<Person> peopleNotUsed) throws CustomException {
        if (db.getConnection() == null) { // check connection state
            return new StrategyResult(false, "You are not connected to the database");
        }

        System.out.println("[Info] Select person's ID from DB to be removed");
        List<Person> people = db.getPeople();
        for (Person p : people) {
            System.out.format("\t[%d] %s%n", p.getID(), p.getFullName());
        }

        int id = University.onlyInt(0, 10000);
        Person p = getPerson(people, id);
        if (p != null) {
            if (p instanceof Teacher) {
                db.removeTeacher(id);
            } else if (p instanceof Student) {
                db.removeStudent(id);
            }
            return new StrategyResult(true, "Person was deleted from SQL DB");
        } else {
            return new StrategyResult(false, "Person with that ID doesn't exist");
        }
    }

    public Person getPerson(List<Person> people, int id) {
        for (Person p : people) if (p.getID() == id) return p;
        return null;
    }
}
