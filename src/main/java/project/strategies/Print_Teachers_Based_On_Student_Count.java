package project.strategies;

import project.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Prints sorted teachers based on theirs student count
 */
public class Print_Teachers_Based_On_Student_Count implements IStrategy {

    @Override
    public StrategyResult execute(List<Person> people) throws CustomException {
        if (people.isEmpty()) {
            return new StrategyResult(false, "There are no people in the university");
        }
        List<Teacher> teachers = new ArrayList<>();

        people.stream().filter(person -> person instanceof Teacher).forEach(person -> teachers.add((Teacher) person));
        List<Teacher> sorted = teachers.stream().sorted(Comparator.comparingInt(
                t -> ((Teacher) t).getStudents().size()).reversed()).collect(Collectors.toList());
        System.out.println("[Info] Sorted teachers by student count");
        for (Teacher t : sorted) {
            System.out.format("\t[%d] %s%n", t.getStudents().size(), t.getFullName());
        }
        return new StrategyResult(true, "Null");
    }
}
