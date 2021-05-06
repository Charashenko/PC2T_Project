package project.strategies;

import project.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Removes a specified person from university
 */
public class Rem_Person_From_University implements IStrategy {
    @Override
    public StrategyResult execute(List<Person> people) throws CustomException {
        // check people in uni
        if (people.isEmpty()) {
            return new StrategyResult(false, "Nobody is in the university");
        }

        // print choices for available people and get ID
        System.out.println("[Info] Select ID of person to be removed");
        for (Person p : people) {
            System.out.format("\t[%d] %s (%s)%n", p.getID(), p.getFullName(), p.getClass().getSimpleName());
        }
        int id = University.onlyInt(0, 10000);
        Person p = University.getPerson(id);

        // if specified person exists
        if (p != null) {
            // if specified person is teacher and his student doesn't have more teachers
            if (p instanceof Teacher) {
                // get problematic students
                List<Student> problematicStudents = new ArrayList<>();
                for (Student s : ((Teacher) p).getStudents()) {
                    if (s.getTeachers().contains(p) && s.getTeachers().size() == 1) {
                        problematicStudents.add(s);
                    }
                }

                // if there are problematic students, do not remove person
                if (!problematicStudents.isEmpty()) {
                    System.out.println("[Warning] You can't remove this teacher because shown students won't have a teacher assigned to them");
                    for (Student s : ((Teacher) p).getStudents()) {
                        if (s.getTeachers().contains(p) && s.getTeachers().size() == 1) {
                            System.out.format("\t[%d] %s%n", s.getID(), s.getFullName());
                        }
                    }
                    return new StrategyResult(false, "Action terminated");
                } else {
                    University.remPerson(id);
                    return new StrategyResult(true, "Person was removed");
                }
            } else {
                University.remPerson(id);
                return new StrategyResult(true, "Person was removed");
            }
        } else {
            return new StrategyResult(false, "Person with that ID doesn't exist");
        }
    }
}
