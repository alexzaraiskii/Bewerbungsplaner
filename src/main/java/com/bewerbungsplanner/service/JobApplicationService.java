package com.bewerbungsplanner.service;

import com.bewerbungsplanner.model.JobApplication;

import java.util.List;

public interface JobApplicationService {

    List<JobApplication> findAll();

    JobApplication save(JobApplication jobApplication);

    void delete(int id);

    JobApplication update(JobApplication jobApplication);

    JobApplication findById(int id);

    List<JobApplication> findJobApplicationsByUserId(int id);

    List<JobApplication> findJobApplicationsByUsername(String username);

    JobApplication addJobApplication(int userId, JobApplication jobApplication);
}
