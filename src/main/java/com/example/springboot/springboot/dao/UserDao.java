package com.example.springboot.springboot.dao;

import java.util.List;

import com.example.springboot.springboot.models.User;

public interface UserDao {
    List<User> getAll();

    User getById(long id) throws Exception;

    User login(String email) throws Exception;

    User create(User user);

    User update(User user) throws Exception;

    User delete(long id) throws Exception;
}