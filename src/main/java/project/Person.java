package project;

public interface Person {

    double DPH = 21;
    double scholarshipNeededAvg = 3;
    double scholarship = 125.5;
    double salaryForOneStudent = 200;

    String getFullName();
    String getName();
    void setName(String name);
    String getSurname();
    void setSurname(String surname);
    String getBirthDate();
    void setBirthDate(String birthdate);
    int getID();
    void setID(int id);
    double getSalary();
    void setSalary(double salary);
    int generateID();
    void printInfo();

}
