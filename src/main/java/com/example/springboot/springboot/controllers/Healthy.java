package com.example.springboot.springboot.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Healthy {

    @GetMapping
    public String get() {
        return "hi!";
    }
}
