import exceptions.InvalidCredentialsException;
import users.Admin;
import users.Student;
import users.Teacher;
import utils.Grade;
import utils.UserType;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Resolver implements Runnable {

    private static class Prompts{

        private static String loginPrompt(){
            return "Enter username and password: ";
        }
        private static String enterGradePrompt(){
            return "Submit a new grade: ";
        }
        private static String enterStudentNumberPrompt(){
            return "Enter student number: ";
        }
        private static String welcomePrompt(UserType type) {
            return switch (type) {
                case ADMIN -> "Welcome, admin.";
                case STUDENT -> "Welcome, student.";
                case TEACHER -> "Welcome, teacher.";
            };
        }
    }

    private final Socket client;
    Admin admin = Admin.getInstance();

    public Resolver(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        BufferedReader reader;
        PrintWriter writer;
        Console console;

        try {
            writer = new PrintWriter(client.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            console = System.console();

            writer.println(Prompts.loginPrompt());
            String username = reader.readLine();

            char[] pass  =console.readPassword();
            String password = new String(pass);
            
            UserType userType = checkCredentials(username, password);
            switch (Objects.requireNonNull(userType)) {
                case ADMIN -> openAdminCommunicationPanel(writer, reader);
                case TEACHER -> {
                    Teacher teacher = (Teacher) admin.createUser(userType, username, password);
                    openTeacherCommunicationPanel(writer, reader, teacher);
                }
                case STUDENT -> {
                    Student student = (Student) admin.createUser(userType, username, password);
                    openStudentCommunicationPanel(writer, student);
                }
                default -> throw new InvalidCredentialsException();
            }
        } catch (IOException | NullPointerException | InvalidCredentialsException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private UserType checkCredentials(String username, String password) {
        if (username.equals("admin") && password.equals("admin")) {
            return UserType.ADMIN;
        }
        if (Teacher.isValidUserName(username) && Teacher.isValidPassword(password)) {
            return UserType.TEACHER;
        }
        if (Student.isValidUserName(username) && Student.isValidPassword(password)) {
            return UserType.STUDENT;
        }
        return null;
    }

    /* communication panels */
    private void openAdminCommunicationPanel(PrintWriter writer, BufferedReader reader) {
        try {
            writer.println(Prompts.welcomePrompt(UserType.ADMIN));
            String userType = reader.readLine();
            if (userType.equalsIgnoreCase(String.valueOf(UserType.TEACHER))) {
                admin.createUser(UserType.TEACHER, reader.readLine(), reader.readLine());
            }
            if (userType.equalsIgnoreCase(String.valueOf(UserType.STUDENT))) {
                admin.createUser(UserType.STUDENT, reader.readLine(), reader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openStudentCommunicationPanel(PrintWriter writer, Student student) {
        writer.println(Prompts.welcomePrompt(UserType.STUDENT));
        writer.println(student.getSortedGrades());
    }

    private void openTeacherCommunicationPanel(PrintWriter writer, BufferedReader reader, Teacher teacher) {
        try {
            writer.println(Prompts.welcomePrompt(UserType.TEACHER));

            writer.println(Prompts.enterStudentNumberPrompt());
            String facNumber = reader.readLine();

            writer.println(Prompts.enterGradePrompt());
            Grade grade = new Grade(reader.readLine(),
                    Double.parseDouble(reader.readLine()),
                    Integer.parseInt(reader.readLine()));
            teacher.addGrade(facNumber, grade);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}