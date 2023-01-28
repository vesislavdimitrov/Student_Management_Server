package users;

import utils.UserType;

import java.io.Serializable;

public abstract class User implements Serializable {
    private final String userName;
    private final String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /* Getters  & Setters */
    public abstract UserType getUserType();
    public String getUser() {
        return userName;
    }
    public String getPassword() {
        return password;
    }

}