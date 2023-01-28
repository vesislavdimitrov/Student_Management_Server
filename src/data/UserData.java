package data;

import users.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserData {
    public static List<User> users = Collections.synchronizedList(new ArrayList<>());
    private static final Lock lock = new ReentrantLock();

    public static void saveUsers(List<User> users) {
        if(users == null){
            throw new IllegalArgumentException("Users list can't be null");
        }
        lock.lock();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("user.bin"))) {
            objectOutputStream.writeObject(users);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not save users, file not found");
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while saving the users: " + e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    public static List<User> loadUsers() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("user.bin"))) {
            Object obj = objectInputStream.readObject();
            if (obj instanceof List<?>) {
                return Collections.unmodifiableList((List<User>) obj);
            } else {
                throw new IllegalArgumentException("The file does not contain a List of User objects.");
            }
        } catch (FileNotFoundException e) {
            return Collections.emptyList();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("An error occurred while reading the file: " + e.getMessage(), e);
        }
    }

}