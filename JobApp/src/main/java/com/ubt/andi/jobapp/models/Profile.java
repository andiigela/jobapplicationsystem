package com.ubt.andi.jobapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String description;
    private String education;
    private String experience;
    private String profession;
    private byte[] imageData;
    private String imagePath;
    private String phone;
    private String experienceLevel;
    private Integer hourlyRate;
    private Integer totalProjects;
    private String englishLevel;
    private String githubLink;

    @OneToOne
    private AppUser appuser;
}
