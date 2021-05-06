package project.strategies;

import project.*;

import java.util.List;

/**
 * Assigns student to specified teacher
 */
public class Assignment_Of_Students_To_Teacher implements IStrategy {

    @Override
    public StrategyResult execute(List<Person> people) throws CustomException {
        if (University.getTeachers().size() == 0) {
            return new StrategyResult(false, "There are no teachers in the university");
        }
        System.out.println("[Info] Choose teacher's ID");
        for (Teacher t : University.getTeachers()) {
            System.out.format("\t[%d] %s%n", t.getID(), t.getFullName());
        }
        int id = University.onlyInt(0, 10000);
        Teacher t = University.getTeacher(id);
        if (t == null) {
            return new StrategyResult(false, "Teacher with this ID doesn't exist");
        }
        System.out.println("[Info] Select action");
        System.out.println("\t[1] Add student");
        System.out.println("\t[2] Remove student");
        int option = University.onlyInt(1, 2);
        System.out.println("[Info] Available students");
        if (option == 1) {
            for (Student s : University.getStudents()) {
                if (!t.getStudents().contains(s)) {
                    System.out.format("\t[%d] %s%n", s.getID(), s.getFullName());
                }
            }
            id = University.onlyInt(0, 10000);
            if (University.getStudent(id) == null) {
                return new StrategyResult(false, "Student with specified ID doesn't exist");
            } else {
                University.getStudent(id).addTeacher(t);
                return new StrategyResult(true, "Student was successfully assigned to teacher");
            }
        } else {
            for (Student s : University.getStudents()) {
                if (t.getStudents().contains(s)) {
                    System.out.format("\t[%d] %s%n", s.getID(), s.getFullName());
                }
            }
            id = University.onlyInt(0, 10000);
            if (University.getStudent(id) == null) {
                return new StrategyResult(false, "Student with specified ID doesn't exist");
            } else {
                University.getStudent(id).remTeacher(t);
                return new StrategyResult(true, "Student was successfully removed from teacher");
            }
        }
    }
}
