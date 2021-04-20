package project.strategies.db;

import project.*;
import project.db.Database;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Connect_to_SQL_database implements IStrategy {

    private final Database db;
    private final HashMap<String, String> DBusers = new HashMap<>(); // authorized users credentials (not safe)

    public Connect_to_SQL_database(Database db) {
        this.db = db;
        DBusers.put("admin", "pc2t");
    }

    @Override
    public StrategyResult execute(List<Person> people) throws CustomException {
        try (Scanner sc = new Scanner(new File("src/main/java/project/db/access_credentials.txt"))) {
            sc.useDelimiter("PATH: |USER: |PASS: ");
            String path = sc.next().stripTrailing();
            String user = sc.next().stripTrailing();
            String pass = sc.next().stripTrailing();
            System.out.format("[Info] Connecting to DB with:%n" +
                    "\t[path] %s%n" +
                    "\t[username] %s%n" +
                    "\t[password] %s%n", path, user, pass);
            if (DBusers.get(user).equals(pass)) {
                db.setAccessible(true);
                db.setPath(path);
                return new StrategyResult(true, "Connection successful");
            }
            return new StrategyResult(false, "Wrong credentials");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
