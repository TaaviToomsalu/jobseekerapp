package com.taavi.jobseekerapp.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello, Job Seeker app! Its the test!";
    }
}
