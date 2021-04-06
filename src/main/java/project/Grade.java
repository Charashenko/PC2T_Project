package project;

public class Grade {

    private int grade;
    private String subject;

    public Grade(String subject, int grade) {
//        if (grade > 5 || grade < 1)
//            throw new project.CustomException("[Warning] Wrong grade (can be 1,2,3,4,5)");
        this.grade = grade;
        this.subject = subject;
    }

    public int getGrade() {
        return grade;
    }

    public String getSubject() {
        return subject;
    }
}
