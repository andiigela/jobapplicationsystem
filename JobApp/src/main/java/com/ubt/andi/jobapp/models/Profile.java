package com.ubt.andi.jobapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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
    private boolean followedByLoggedInUser=false;
    private Integer followersNumber=0;
    private Integer followingsNumber=0;
    private Integer notificationsNumber=0;
    @OneToOne
    private AppUser appuser;
    @OneToMany(mappedBy = "follower")
    private List<Follow> followers = new ArrayList<>();
    @OneToMany(mappedBy = "following")
    private List<Follow> followings = new ArrayList<>();
    @OneToMany(mappedBy = "fromProfile")
    private List<Notification> fromProfileNotifications = new ArrayList<>();
    @OneToMany(mappedBy = "toProfile")
    private List<Notification> toProfileNotifications = new ArrayList<>();
    @OneToOne(mappedBy = "profile",cascade = CascadeType.REMOVE)
    private Interview interview;

}
