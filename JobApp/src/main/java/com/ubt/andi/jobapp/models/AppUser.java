package com.ubt.andi.jobapp.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name = "role_account")
    private Boolean roleAccount; // 1 = jobseeker, 2 = employee
    @CreationTimestamp
    @Column(name = "date_created")
    private Date dateCreated;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns= @JoinColumn(name = "appuser_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();
    @OneToOne(mappedBy = "appuser")
    private Profile profile;
    @OneToMany(mappedBy = "appUser")
    private List<Job> jobs = new ArrayList<>();
    @OneToMany(mappedBy = "appUser")
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "appUser")
    private List<LikedPosts> likedPosts = new ArrayList<>();
    @OneToMany(mappedBy = "appUser")
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "appUser")
    private List<Application> applications = new ArrayList<>();
    @OneToMany(mappedBy = "appUser")
    private List<Share>shares = new ArrayList<>();
}
