package com.example.springboot.springboot.models;

import lombok.Getter;
import lombok.Setter;

public class UserLogin {
    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String token;

    public UserLogin(User user, String token) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.token = token;
    }
}
