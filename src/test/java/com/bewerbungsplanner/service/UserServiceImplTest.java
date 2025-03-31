package com.bewerbungsplanner.service;

import com.bewerbungsplanner.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    UserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void save() {
        userService.save(new User("julia", passwordEncoder.encode("123")));
    }

    @Test
    void findByName() {
        userService.findByName("julia");
    }

    @Test
    void findById() {
        userService.findById(1);
    }
}