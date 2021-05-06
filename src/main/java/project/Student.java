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

    /**
     * Constructor for creating a student from application
     * @param name Name of student
     * @param surname Surname of student
     * @param birthDate Birthdate of student
     * @param teacher One teacher of student
     */
    public Student(String name, String surname, String birthDate, Teacher teacher) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.ID = generateID();
        teachers.add(teacher);
        teacher.addStudent(this);
    }

    /**
     * Constructor for creating a student from database
     * @param id ID of student
     * @param name Name of student
     * @param surname Surname of student
     * @param birthDate Birthdate of student
     * @param salary Salary of student
     * @param avg Study average of student
     */
    public Student(int id, String name, String surname, String birthDate, double salary, double avg) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.ID = id;
        this.salary = salary;
        this.avg = avg;
    }

    /**
     * Gets student's full name
     * @return Full name of student
     */
    @Override
    public String getFullName() {
        return name + " " + surname;
    }

    /**
     * Gets student's name
     * @return Name of student
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets student's name
     * @param name Name to be set
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets student's surname
     * @return Surname of student
     */
    @Override
    public String getSurname() {
        return surname;
    }

    /**
     * Sets student's surname
     * @param surname Surname to be set
     */
    @Override
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets student's birthdate
     * @return Birthdate of student
     */
    @Override
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets student's birthday
     * @param birthDate Birthdate to be set
     */
    @Override
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets student's ID
     * @return ID of student
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * Sets student's id
     * @param ID ID to be set
     */
    @Override
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Return student's salary if student has needed study average
     * @return Salary
     */
    @Override
    public double getSalary() { //
        if (getAvg() < Person.scholarshipNeededAvg) {
            salary = Person.scholarship;
            return salary;
        }
        return 0;
    }

    /**
     * Sets student's salary
     * @param salary Salary to be set
     */
    @Override
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Generate unique ID based on time attributes and name and surname length
     * @return unique ID
     */
    @Override
    public int generateID(){
        LocalDateTime d = LocalDateTime.now();
        return d.getYear() + d.getDayOfYear() + d.getHour() + d.getMinute() + d.getSecond()
                + getName().length() * getSurname().length();
    }

    /**
     * Compares object to student based on ID, name, surname, birthdate and salary
     * @param o Student object
     * @return True if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return ID == student.ID && Double.compare(student.salary, salary) == 0 && name.equals(student.name)
                && surname.equals(student.surname) && birthDate.equals(student.birthDate);
    }

    /**
     * Calculates hashcode from name, surname and birthdate
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, surname, birthDate);
    }

    /**
     * Prints all information about student
     */
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

    /**
     * Gets student's teachers
     * @return teachers
     */
    public List<Teacher> getTeachers() {
        return teachers;
    }

    /**
     * Adds teacher to student
     * @param t Teacher to be added
     */
    public void addTeacher(Teacher t){
        if (!teachers.contains(t)) {
            teachers.add(t);
            t.addStudent(this);
        }
    }

    /**
     * Removes teacher from student
     * @param t Teacher to be removed
     */
    public void remTeacher(Teacher t){
        teachers = teachers.stream().filter(o -> o.getID() != t.getID()).collect(Collectors.toList());
    }

    /**
     * Gets student's grades
     * @return Grades
     */
    public List<Grade> getGrades() {
        return grades;
    }

    /**
     * Adds grade to student
     * @param g Grade to be added
     */
    public void addGrade(Grade g){
        grades.add(g);
    }

    /**
     * Removes grade from student
     * @param g Grade to be removed
     */
    public void remGrade(Grade g){
        grades.remove(g);
    }

    /**
     * Gets student's study average
     * @return Average
     */
    public double getAvg(){
        int grades = 0;
        int numOfGrades = 0;
        for (Grade g : getGrades()) {
            grades += g.getGrade();
            numOfGrades += 1;
        }
        return (double) grades/numOfGrades;
    }

    /**
     * Sets student's average
     * @param avg Average to be set
     */
    public void setAvg(double avg) {
        this.avg = avg;
    }

    /**
     * Sets student's teachers
     * @param teachers Teacher list to be set
     */
    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    /**
     * Sets student's grades
     * @param grades Grade list to be set
     */
    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}
