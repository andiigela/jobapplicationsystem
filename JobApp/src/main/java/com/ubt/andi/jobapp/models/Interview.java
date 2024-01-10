package com.ubt.andi.jobapp.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Table
@Entity
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String text;
    private String interviewerName;
    private LocalDateTime date;
    @OneToOne
    private Job job;
    @OneToOne
    private Profile profile;
}

