package com.bewerbungsplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String company;

    private String position;

    private String status;

    private String dateOfApplication;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String comments;

    private String jobApplicationLink;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    public JobApplication(String company, String position, String status, String dateOfApplication, String jobDescription, String comments, String jobApplicationLink) {
        this.company = company;
        this.position = position;
        this.status = status;
        this.dateOfApplication = dateOfApplication;
        this.jobDescription = jobDescription;
        this.comments = comments;
        this.jobApplicationLink = jobApplicationLink;
    }
}
