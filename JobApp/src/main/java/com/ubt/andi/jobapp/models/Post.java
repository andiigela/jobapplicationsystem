package com.ubt.andi.jobapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

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
    @UpdateTimestamp
    private LocalDate updatedAt;
    @ManyToOne
    private AppUser appUser;
    @OneToMany(mappedBy = "post")
    private List<LikedPosts> likedPosts = new ArrayList<>();
}
