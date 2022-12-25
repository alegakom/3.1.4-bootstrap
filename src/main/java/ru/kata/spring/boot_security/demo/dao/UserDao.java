package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers ();
    Role getRoleByName(String role);
    List<Role> getAllRoles();
    void saveUser(User user);
    void removeUser(long id);
    void updateUser(User user);
    public String encode(CharSequence password);
    User getUserById(long id);
    User findByUsername (String username);
}
