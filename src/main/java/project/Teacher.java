package project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Teacher implements Person {

    private int ID;
    private String name;
    private String surname;
    private String birthDate;
    private double salary;
    private List<Student> students;

    /**
     * Constructor for creating Teacher from code
     * @param name Teacher's name
     * @param surname Teahcer's surname
     * @param birthDate Teacher's birthdate
     */
    public Teacher(String name, String surname, String birthDate) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.ID = generateID();
        students = new ArrayList<>();
    }

    /**
     * Constructor for creating Teacher from database
     * @param id Teacher's ID
     * @param name Teacher's name
     * @param surname Teacher's surname
     * @param birthDate Teacher's birthdate
     * @param salary Teacher's salary
     */
    public Teacher(int id, String name, String surname, String birthDate, double salary) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.ID = id;
        this.salary = salary;
        students = new ArrayList<>();
    }

    /**
     * Gets teacher's full name
     * @return Full name
     */
    @Override
    public String getFullName() {
        return name + " " + surname;
    }

    /**
     * Gets teacher's name
     * @return Name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets teacher's name
     * @param name Name to be set
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets teacher's surname
     * @return Surname
     */
    @Override
    public String getSurname() {
        return surname;
    }

    /**
     * Sets teacher's surname
     * @param surname Surname to be set
     */
    @Override
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets teacher's birthdate
     * @return Birthdate
     */
    @Override
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets teacher's birthdate
     * @param birthDate Birthdate to be set
     */
    @Override
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets teacher's ID
     * @return ID
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * Sets teacher's ID
     * @param id ID to be set
     */
    @Override
    public void setID(int id){
        this.ID = id;
    }

    /**
     * Gets teacher's salary
     * @return Salary
     */
    @Override
    public double getSalary() {
        this.salary = students.size() * Person.salaryForOneStudent;
        return salary;
    }

    /**
     * Sets teacher's salary
     * @param salary
     */
    @Override
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Generates ID for teacher
     * @return Generated ID
     */
    @Override
    public int generateID(){
        LocalDateTime d = LocalDateTime.now();
        return d.getYear() + d.getDayOfYear() + d.getHour() + d.getMinute() + d.getSecond()
                + getName().length() * getSurname().length();
    }

    /**
     * Gets teacher's students
     * @return List of teacher's students
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Sets teacher's students
     * @param students List of students to be added
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /**
     * Adds student to teacher
     * @param s Student to be added
     */
    public void addStudent(Student s){
        if(!students.contains(s))
            students.add(s);
    }

    /**
     * Removes teacher's student
     * @param s Student to be removed
     */
    public void remStudent(Student s){
        students.remove(s);
        students = students.stream().filter(o -> o.getID() != s.getID()).collect(Collectors.toList());
    }

    /**
     * Prints teacher info
     */
    public void printInfo(){
        System.out.println("[Teacher] " + getFullName());
        System.out.println("\t[ID] " + getID());
        System.out.println("\t[Date of Birth] " + getBirthDate());
        System.out.format("\t[Salary] %.2f%n", getSalary()/(100+DPH)*100);
        System.out.println("\t[Number of students] " + getStudents().size());
    }

}
