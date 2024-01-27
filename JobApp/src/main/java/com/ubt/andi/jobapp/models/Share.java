package com.ubt.andi.jobapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    @CreationTimestamp
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)  // Consider FetchType.LAZY based on your use case
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)  // Consider FetchType.LAZY based on your use case
    private Post post;
}


