package com.ubt.andi.jobapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String Location;
    @CreationTimestamp
    @DateTimeFormat(pattern = "MM-dd-yyyy") // Specify the date format
    private LocalDate dateCreated;
    @DateTimeFormat(pattern = "MM-dd-yyyy") // Specify the date format
    private LocalDate expirationDate;
    private boolean active;
    @ManyToOne
    private AppUser appUser;
    @OneToMany(mappedBy = "job",cascade = CascadeType.ALL)
    private List<Application> applications = new ArrayList<>();
    @OneToOne(mappedBy = "job",cascade = CascadeType.REMOVE)
    private Post post;
}
