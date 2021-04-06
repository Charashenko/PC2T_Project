package project.strategies;

import project.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Print_People_Sorted_By_Surname implements IStrategy {
    @Override
    public StrategyResult execute(List<Person> people) throws CustomException {
        if (people.isEmpty()) {
            return new StrategyResult(false, "There are no people in the university");
        }
        Comparator<Person> personComparator = Comparator.comparing(Person::getSurname);
        List<Person> sorted = people.stream().sorted(personComparator).collect(Collectors.toList());
        for (Teacher t : extractTeachers(sorted)) {
            t.printInfo();
        }
        for (Student s : extractStudents(sorted)) {
            s.printInfo();
        }
        return new StrategyResult(true, "Null");
    }

    public static List<Teacher> extractTeachers(List<Person> people) {
        List<Teacher> teachers = new ArrayList<>();
        for (Person p : people.stream().filter(o -> o instanceof Teacher).collect(Collectors.toList())) {
            teachers.add((Teacher) p);
        }
        return teachers;
    }

    public static List<Student> extractStudents(List<Person> people) {
        List<Student> students = new ArrayList<>();
        for (Person p : people.stream().filter(o -> o instanceof Student).collect(Collectors.toList())) {
            students.add((Student) p);
        }
        return students;
    }
}
