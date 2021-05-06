package project.strategies;


import project.*;

import java.util.List;
import java.util.Scanner;

/**
 * Adds person to the university
 */
public class Add_Person_To_University implements IStrategy {

    @Override
    public StrategyResult execute(List<Person> people) throws CustomException {
        Scanner sc = new Scanner(System.in);
        System.out.println("[Info] Choose who to add:");
        System.out.println("\t[1] Teacher");
        System.out.println("\t[2] Student");
        int option = University.onlyInt(1, 2);
        String name, surname, date;
        if (option == 1) { // adds a teacher
            System.out.println("[Info] Add name, surname and date of birth");
            name = sc.nextLine();
            surname = sc.nextLine();
            date = sc.nextLine();
            people.add(new Teacher(name, surname, date));
            return new StrategyResult(true, "Teacher was added");
        } else { // adds a student
            if (University.getTeachers().size() == 0) {
                return new StrategyResult(false, "You can't add student because there are no available teachers");
            }
            System.out.println("[Info] Add name, surname, date of birth and ID of teacher (showing available teachers)");
            for (Teacher t : University.getTeachers()) {
                System.out.format("\t[%d] %s%n", t.getID(), t.getFullName());
            }
            name = sc.nextLine();
            surname = sc.nextLine();
            date = sc.nextLine();
            int id = University.onlyInt(0, 100000);
            if (University.getTeacher(id) != null) {
                people.add(new Student(name, surname, date, University.getTeacher(id)));
                return new StrategyResult(true, "Student was added");
            } else {
                return new StrategyResult(false, "Teacher with specified ID doesn't exist");
            }
        }
    }
}
