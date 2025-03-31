package com.bewerbungsplanner.dao;

import com.bewerbungsplanner.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JobApplicationDAO extends JpaRepository<JobApplication, Integer> {


}
