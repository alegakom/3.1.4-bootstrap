package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<User> getAllUsers() {
        String sql = "select u from User u";
        return entityManager.createQuery(sql, User.class).getResultList();
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void removeUser(long id) {

        entityManager.remove(getUserById(id));
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public User getUserById(long id) {
        return entityManager.find(User.class,id);
    }

    @Override
    public User findByUserName(String username) {
        try{
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username", User.class);
            return query.setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e){
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }

    }
}

