package com.bewerbungsplanner.service;

import com.bewerbungsplanner.dao.JobApplicationDAO;
import com.bewerbungsplanner.dao.UserDAO;
import com.bewerbungsplanner.model.JobApplication;
import com.bewerbungsplanner.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    @Autowired
    private JobApplicationDAO jobApplicationDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<JobApplication> findAll() {
        return jobApplicationDAO.findAll();
    }

    @Override
    public JobApplication save(JobApplication jobApplication) {
        return jobApplicationDAO.save(jobApplication);
    }

    @Override
    public void delete(int id) {
        jobApplicationDAO.deleteById(id);
    }


    @Override
    public JobApplication update(JobApplication jobApplication) {

        return jobApplicationDAO.save(jobApplication);
    }


    @Override
    public JobApplication findById(int id) {
        return jobApplicationDAO.findById(id).orElse(null);
    }

    @Override
    public List<JobApplication> findJobApplicationsByUserId(int id) {
        User user = userDAO.findById(id).orElse(null);
        if (user == null) {
            throw new NoSuchElementException("User not found: " + id);
        }
        return user.getJobApplications();
    }

    @Override
    public List<JobApplication> findJobApplicationsByUsername(String username) {
        return null;
    }

    @Override
    public JobApplication addJobApplication(int userId, JobApplication jobApplication) {
        User user = userDAO.findById(userId).orElse(null);
        if (user == null) {
            throw new NoSuchElementException("User not found: " + userId);
        }
        user.getJobApplications().add(jobApplication);

        return userDAO.save(user) != null ? jobApplication : null;
    }
}
