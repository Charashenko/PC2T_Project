package project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Teacher implements Person {

    private int ID;
    private final String name;
    private final String surname;
    private final String birthDate;
    private double salary;
    private List<Student> students;

    public Teacher(String name, String surname, String birthDate) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.ID = generateID();
        students = new ArrayList<>();
    }

    public Teacher(int id, String name, String surname, String birthDate, double salary) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.ID = id;
        this.salary = salary;
        students = new ArrayList<>();
    }

    @Override
    public String getFullName() {
        return name + " " + surname;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public String getBirthDate() {
        return birthDate;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void setID(int id){
        this.ID = id;
    }

    @Override
    public double getSalary() {
        this.salary = students.size() * Person.salaryForOneStudent;
        return salary;
    }

    @Override
    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public int generateID(){
        LocalDateTime d = LocalDateTime.now();
        return d.getYear() + d.getDayOfYear() + d.getHour() + d.getMinute() + d.getSecond() + new Random().nextInt(1000);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student s){
        if(!students.contains(s))
            students.add(s);
    }

    public void remStudent(Student s){
        students = students.stream().filter(o -> o.getID() != s.getID()).collect(Collectors.toList());
    }

    public void printInfo(){
        System.out.println("[Teacher] " + getFullName());
        System.out.println("\t[ID] " + getID());
        System.out.println("\t[Date of Birth] " + getBirthDate());
        System.out.format("\t[Salary] %.2f%n", getSalary()/(100+DPH)*100);
        System.out.println("\t[Number of students] " + getStudents().size());
    }


}
