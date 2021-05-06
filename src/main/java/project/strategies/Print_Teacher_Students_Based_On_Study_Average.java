package project.strategies;

import project.*;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Prints sorted students of one teacher based on their study average
 */
public class Print_Teacher_Students_Based_On_Study_Average implements IStrategy {
    @Override
    public StrategyResult execute(List<Person> people) throws CustomException {
        if (people.isEmpty()) {
            return new StrategyResult(false, "There are no people in the university");
        }
        System.out.println("[Info] Select teacher ID");
        for (Teacher t : University.getTeachers()) {
            System.out.format("\t[%d] %s%n", t.getID(), t.getFullName());
        }
        int id = University.onlyInt(0, 10000);
        Teacher t = University.getTeacher(id);
        if (t == null) {
            return new StrategyResult(false, "Teacher with that ID doesn't exist");
        } else if (t.getStudents().isEmpty()){
            return new StrategyResult(false, "Teacher has no assigned students");
        }
        Comparator<Student> studentComparator = (o1, o2) -> {
            double diff = o1.getAvg() - o2.getAvg();
            if (diff > 0){
                return 1;
            } else if (diff < 0){
                return -1;
            } else {
                return 0;
            }
        };
        List<Student> sorted = t.getStudents().stream().sorted(studentComparator).collect(Collectors.toList());
        System.out.println("[Info] Sorted students by study average");
        for (Student s : sorted) {
            System.out.format("\t[%.2f] %s%n", s.getAvg(), s.getFullName());
        }
        return new StrategyResult(true, "Null");
    }
}
