package project;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Student implements Person {

    private int ID;
    private String name;
    private String surname;
    private String birthDate;
    private double salary;
    private double avg;
    private List<Teacher> teachers = new ArrayList<>();
    private List<Grade> grades = new ArrayList<>();

    public Student(String name, String surname, String birthDate, Teacher teacher) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.ID = generateID();
        teachers.add(teacher);
        teacher.addStudent(this);
    }

    public Student(int id, String name, String surname, String birthDate, double salary, double avg) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.ID = id;
        this.salary = salary;
        this.avg = avg;
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
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String getBirthDate() {
        return birthDate;
    }

    @Override
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public double getSalary() {
        if (getAvg() < Person.scholarshipNeededAvg) {
            salary = Person.scholarship;
            return salary;
        }
        return 0;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return ID == student.ID && Double.compare(student.salary, salary) == 0 && name.equals(student.name) && surname.equals(student.surname) && birthDate.equals(student.birthDate) && Objects.equals(teachers, student.teachers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, birthDate);
    }

    @Override
    public void printInfo(){
        System.out.println("[Student] " + getFullName());
        System.out.println("\t[ID] " + getID());
        System.out.println("\t[Date of Birth] " + getBirthDate());
        System.out.format("\t[Salary] %.2f%n", getSalary());
        System.out.println("\t[Number of teachers] " + getTeachers().size());
        System.out.format("\t[Study Average] %.2f%n", getAvg());
        System.out.println("\t[Grades]");
        for (Grade g : getGrades()) {
            System.out.format("\t\t[%s] %d%n", g.getSubject(), g.getGrade());
        }
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void addTeacher(Teacher t){
        if (!teachers.contains(t)) {
            teachers.add(t);
            t.addStudent(this);
        }
    }

    public void remTeacher(Teacher t){
        teachers = teachers.stream().filter(o -> o.getID() != t.getID()).collect(Collectors.toList());
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void addGrade(Grade g){
        grades.add(g);
    }

    public void remGrade(Grade g){
        grades.remove(g);
    }

    public double getAvg(){
        int grades = 0;
        int numOfGrades = 0;
        for (Grade g : getGrades()) {
            grades += g.getGrade();
            numOfGrades += 1;
        }
        return (double) grades/numOfGrades;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}
