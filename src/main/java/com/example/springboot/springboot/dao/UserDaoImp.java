package com.example.springboot.springboot.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.springboot.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class UserDaoImp implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAll() {
        return entityManager.createQuery("Select u From User u", User.class).getResultList();
    }

    @Override
    public User getById(long id) throws Exception {
        User user = entityManager.find(User.class, id);

        if (user == null) {
            throw new Exception("user with id %s, not exist".formatted(id));
        }
        return user;
    }

    @Override
    public User login(String email) throws Exception {
        User user = entityManager
                .createQuery("From User Where email = :email", User.class)
                .setParameter("email", email)
                .getSingleResultOrNull();

        return user;
    }

    @Override
    public User create(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public User update(User user) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public User delete(long id) throws Exception {
        User user = this.getById(id);
        entityManager.remove(user);
        return user;
    }

}
