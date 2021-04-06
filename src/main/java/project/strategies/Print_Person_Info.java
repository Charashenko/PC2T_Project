package project.strategies;

import project.*;

import java.util.List;

public class Print_Person_Info implements IStrategy {

    @Override
    public StrategyResult execute(List<Person> people) throws CustomException {
        if (people.isEmpty()) {
            return new StrategyResult(false, "There are no people in the university");
        }
        System.out.println("[Info] Choose who to print");
        System.out.println("\t[1] Teacher");
        System.out.println("\t[2] Student");
        int option = University.onlyInt(1, 2);
        System.out.println("[Info] Select from available IDs");

        if (option == 1) {
            for (Teacher t : University.getTeachers()) {
                System.out.format("\t[%d] %s%n", t.getID(), t.getFullName());
            }
            int id = University.onlyInt(0, 10000);
            Teacher t = University.getTeacher(id);
            if (t != null) {
                t.printInfo();
            } else {
                return new StrategyResult(false, "Nobody with that ID exists");
            }
        } else {
            for (Student s : University.getStudents()) {
                System.out.format("\t[%d] %s%n", s.getID(), s.getFullName());
            }
            int id = University.onlyInt(0, 10000);
            Student s = University.getStudent(id);
            if (s != null) {
                s.printInfo();
            } else {
                return new StrategyResult(false, "Nobody with that ID exists");
            }
        }
        return new StrategyResult(true, "Null");
    }

}
