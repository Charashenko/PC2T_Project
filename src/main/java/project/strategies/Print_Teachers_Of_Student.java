package project.strategies;

import project.*;

import java.util.List;

/**
 * Prints all teachers of specified student
 */
public class Print_Teachers_Of_Student implements IStrategy {

    @Override
    public StrategyResult execute(List<Person> people) throws CustomException {
        if (University.getStudents().size() == 0) {
            return new StrategyResult(false, "There are no students in the university");
        }
        System.out.println("[Info] Choose student's ID");
        for (Student s : University.getStudents()) {
            System.out.format("\t[%d] %s%n", s.getID(), s.getFullName());
        }
        int id = University.onlyInt(0, 10000);
        if (University.getStudent(id) != null) {
            System.out.println("[Info] Teachers of this student");
            for (Teacher t : University.getStudent(id).getTeachers()) {
                System.out.format("\t[%d] %s%n", t.getID(), t.getFullName());
            }
            return new StrategyResult(true, "Null");
        } else {
            return new StrategyResult(false, "Student with that ID doesn't exist");
        }
    }
}
