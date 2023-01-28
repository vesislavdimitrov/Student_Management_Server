package utils;

import java.io.Serializable;

public class Grade implements Serializable {
    private String subject;
    private double grade;
    private int semester;

    public Grade(String subject, double grade, int semester) {
        this.subject = subject;
        this.grade = grade;
        this.semester = semester;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grade grade1 = (Grade) o;

        if (Double.compare(grade1.grade, grade) != 0) return false;
        if (semester != grade1.semester) return false;
        return subject.equals(grade1.subject);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = subject.hashCode();
        temp = Double.doubleToLongBits(grade);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + semester;
        return result;
    }

    @Override
    public String toString() {
        return "Grade{" + "subject='" + subject + '\'' + ", grade=" + grade + ", semester=" + semester + '}';
    }

    /* Getters  & Setters */
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}