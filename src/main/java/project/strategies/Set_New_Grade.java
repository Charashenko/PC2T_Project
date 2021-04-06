package project.strategies;

import project.*;

import java.util.List;
import java.util.Scanner;

public class Set_New_Grade implements IStrategy {
    @Override
    public StrategyResult execute(List<Person> people) throws CustomException {
        if (University.getStudents().size() == 0) {
            return new StrategyResult(false, "There are no students in the university");
        }
        System.out.println("[Info] Choose student ID");
        Scanner sc = new Scanner(System.in);
        for (Person p : people) {
            if (p instanceof Student) {
                System.out.format("\t[%d] %s%n", p.getID(), p.getFullName());
            }
        }
        int id = University.onlyInt(0, 100000);
        if (University.getStudent(id) == null) {
            return new StrategyResult(false, "Student with specified ID doesn't exist");
        }
        System.out.println("[Info] Choose subject name and grade");
        String sub = sc.nextLine();
        int g = University.onlyInt(1, 5);
        University.getStudent(id).addGrade(new Grade(sub, g));
        return new StrategyResult(true, "Grade was successfully added");
    }
}
