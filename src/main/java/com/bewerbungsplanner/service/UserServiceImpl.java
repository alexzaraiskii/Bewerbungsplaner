package com.bewerbungsplanner.service;

import com.bewerbungsplanner.dao.UserDAO;
import com.bewerbungsplanner.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDAO userDAO;

    @Override
    public User save(User user) {
        return userDAO.save(user);
    }

    @Override
    public User findByName(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public User findById(int id) {
        return userDAO.findById(id).orElse(null);
    }
}
