package com.ubt.andi.jobapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private Long numberOfLikes=0L;
    private Long numberOfComments=0L;
    @CreationTimestamp
    private LocalDate createdAt;
    @UpdateTimestamp
    private LocalDate updatedAt;
    @ManyToOne
    private AppUser appUser;
    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE)
    private List<LikedPosts> likedPosts = new ArrayList<>();
    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();
    @OneToOne
    private Job job;
}
