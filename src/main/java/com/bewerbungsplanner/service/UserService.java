package com.bewerbungsplanner.service;

import com.bewerbungsplanner.model.User;

public interface UserService {

    User save(User user);

    User findByName(String username);

    User findById(int id);
}
