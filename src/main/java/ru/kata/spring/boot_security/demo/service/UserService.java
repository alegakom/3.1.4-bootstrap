package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDao {
    List<User> getAllUsers ();

    @Override
    User findByUsername(String username);

    @Override
    void saveUser(User user);

    @Override
    void removeUser(long id);

    @Override
    void updateUser(User user);

    @Override
    User getUserById(long id);

    @Override
    String encode(CharSequence password);
}
