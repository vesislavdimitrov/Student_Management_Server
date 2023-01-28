package users;

import data.UserData;
import utils.UserType;

public class Admin extends User {
    private static final Admin instance = new Admin("admin", "admin");

    private Admin(String user, String password) {
        super(user, password);
    }

    public User createUser(UserType userType, String user, String password) {
        switch (userType) {
            case TEACHER -> {
                Teacher teacher = new Teacher(user, password);
                UserData.users.add(teacher);
                UserData.saveUsers(UserData.users);
                return teacher;
            }
            case STUDENT -> {
                Student student = new Student(user, password);
                UserData.users.add(student);
                UserData.saveUsers(UserData.users);
                return student;
            }
            default -> {
                return null;
            }
        }
    }

    /* Getters  & Setters */
    public static Admin getInstance() {
        return instance;
    }
    @Override
    public UserType getUserType() {
        return UserType.ADMIN;
    }
}