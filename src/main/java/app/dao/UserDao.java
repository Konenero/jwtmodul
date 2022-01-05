package app.dao;



import app.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    User getUserById(int id);
    User getUserByUsername(String username);
    User saveUser(User user);
    void updateUser(User user);
    void deleteUserById(int id);
}
