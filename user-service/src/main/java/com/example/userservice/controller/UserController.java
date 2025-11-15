package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final String USER_SERVICE = "userService";

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "fallbackForGetUsers")
    public List<User> getUsers() {
        //Simulating a failure
        if (Math.random() > 0.5) {
            throw new RuntimeException("Something went wrong");
        }
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    public List<User> fallbackForGetUsers(Throwable t) {
        return List.of(new User("Fallback User", "fallback@example.com"));
    }
}