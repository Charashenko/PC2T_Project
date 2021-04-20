package project.strategies.db;

import project.*;
import project.db.Database;

import java.util.List;
import java.util.Scanner;

public class Read_All_Data_from_DB implements IStrategy {

    private final Database db;

    public Read_All_Data_from_DB(Database db) {
        this.db = db;
    }

    @Override
    public StrategyResult execute(List<Person> people) throws CustomException { // writes all data from DB into memory
        if (db.getConnection() == null) { // check connection state
            return new StrategyResult(false, "You are not connected to the database");
        }

        // overwrite safety
        Scanner sc = new Scanner(System.in);
        System.out.println("[Warning] This action will overwrite current database in memory, continue? [y/n]");
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

        people.clear();
        people.addAll(db.getPeople());

        return new StrategyResult(true, "Data were read from DB");
    }
}
