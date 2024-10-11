package com.example.springboot.springboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.springboot.dao.UserDao;
import com.example.springboot.springboot.models.User;
import com.example.springboot.springboot.models.UserLogin;
import com.example.springboot.springboot.tools.JWTTool;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JWTTool jwtTool;

    private Argon2 argon = Argon2Factory.create();

    @GetMapping
    public ResponseEntity<?> getAll(@RequestHeader(value = "Authorization") String token) {
        try {
            validToken(token);
            return new ResponseEntity<>(userDao.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id, @RequestHeader(value = "Authorization") String token)
            throws Exception {
        try {
            validToken(token);
            return new ResponseEntity<>(userDao.getById(Long.parseLong(id)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings("deprecation")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) throws Exception {
        try {
            User userFind = userDao.login(user.getEmail());

            if (userFind == null) {
                throw new Exception("User not exist");
            }

            boolean checkPassword = argon.verify(userFind.getPassword(), user.getPassword());

            if (!checkPassword) {
                throw new Exception("Password Incorrect");
            }

            String jwt = jwtTool.create(String.valueOf(userFind.getId()), userFind.getEmail());
            return new ResponseEntity<>(new UserLogin(userFind, jwt), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings("deprecation")
    @PostMapping
    public User CreateOne(@RequestBody User user) {
        user.setPassword(argon.hash(1, 1024, 1, user.getPassword()));
        return userDao.create(user);
    }

    @PutMapping("/{id}")
    public User UpdateOne(@PathVariable String id, @RequestBody User userBody,
            @RequestHeader(value = "Authorization") String token) throws Exception {
        validToken(token);
        userBody.setId(Long.parseLong(id));
        return userDao.update(userBody);
    }

    @DeleteMapping("/{id}")
    public User DeleteOne(@PathVariable String id, @RequestHeader(value = "Authorization") String token)
            throws Exception {
        validToken(token);
        return userDao.delete(Long.parseLong(id));
    }

    private void validToken(String token) throws Exception {
        if (jwtTool.getKey(token) == null) {
            throw new Exception("Token Expired");
        }
    }
}
