package project;

public interface Person {

    double DPH = 21;
    double scholarshipNeededAvg = 3;
    double scholarship = 125.5;
    double salaryForOneStudent = 200;

    String getFullName();
    String getName();
    String getSurname();
    String getBirthDate();
    int getID();
    void setID(int id);
    double getSalary();
    void setSalary(double salary);
    int generateID();
    void printInfo();

}
