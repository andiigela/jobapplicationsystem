package com.ubt.andi.jobapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Table(name = "application")
@Entity
@Getter
@Setter
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    private LocalDate creationDate;
    @ManyToOne
    private AppUser appUser;
    @ManyToOne
    private Job job;
}
