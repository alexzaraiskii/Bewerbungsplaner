package com.bewerbungsplanner.dao;

import com.bewerbungsplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserDAO extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.username = ?1")

    User findByUsername(String username);

}
