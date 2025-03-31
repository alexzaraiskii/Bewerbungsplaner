package com.bewerbungsplanner.service;

import com.bewerbungsplanner.model.JobApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JobApplicationServiceImplTest {

    @Autowired
    JobApplicationService service;

    @Test
    void findJobApplicationsByUserId() {
        List<JobApplication> list =  service.findJobApplicationsByUserId(1);
        System.out.println(list);
    }
}