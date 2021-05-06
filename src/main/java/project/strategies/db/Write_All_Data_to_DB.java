package project.strategies.db;

import project.*;
import project.db.Database;

import java.util.List;
import java.util.Scanner;

/**
 * Writes all data from DB in RAM to sql DB
 */
public class Write_All_Data_to_DB implements IStrategy {

    private final Database db;

    public Write_All_Data_to_DB(Database db) {
        this.db = db;
    }

    @Override
    public StrategyResult execute(List<Person> people) throws CustomException { // writes all data from memory to DB
        // check connection state
        if (db.getConnection() == null) {
            return new StrategyResult(false, "You are not connected to the database");
        }

        // overwrite safety
        Scanner sc = new Scanner(System.in);
        System.out.println("[Warning] This action will overwrite database on disk, continue? [y/n]");
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

        db.clearTables(); // delete everything from DB

        for (Person p : people) {
            // write all teachers to DB
            if (p instanceof Teacher) {
                db.insertTeacher((Teacher) p);
            }
            // write all students to DB
            else {
                db.insertStudent((Student) p);
            }
        }
        return new StrategyResult(true, "Data were written to DB");
    }
}
