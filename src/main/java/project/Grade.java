package project;

public class Grade {

    private int grade;
    private String subject;

    public Grade(String subject, int grade) {
        this.grade = grade;
        this.subject = subject;
    }

    /**
     * Gets grade
     * @return Grade in integer form
     */
    public int getGrade() {
        return grade;
    }

    /**
     * Gets grade's subject
     * @return Subject
     */
    public String getSubject() {
        return subject;
    }
}
