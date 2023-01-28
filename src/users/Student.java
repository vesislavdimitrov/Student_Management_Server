package users;

import utils.Grade;
import utils.UserType;

import java.util.*;
import java.util.regex.Pattern;

public class Student extends User{
    private final ArrayList<Grade> grades;

    public Student(String userName, String password) {
        super(userName, password);
        this.grades = new ArrayList<>();
    }

    public String getSortedGrades() {

        List<Grade> gradesCopy = Collections.synchronizedList(new ArrayList<>(this.grades));
        return gradesCopy.parallelStream()
                .sorted(Comparator.comparing(Grade::getSemester)
                        .thenComparing(Grade::getSubject))
                .toString();
    }

    /* regex validation */
    public static boolean isValidUserName(String userNameName) {
        Pattern pattern = Pattern.compile("[0-9]{9}");
        return pattern.matcher(userNameName).matches();
    }
    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("[0-9]{10}");
        return pattern.matcher(password).matches();
    }


    /* Getters  & Setters */
    @Override
    public UserType getUserType() {
        return UserType.STUDENT;
    }
    public ArrayList<Grade> getGrades() {
        return grades;
    }
    public void setGrade(Grade grade) {
        this.grades.add(grade);
    }

}