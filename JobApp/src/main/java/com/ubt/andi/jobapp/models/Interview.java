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
    private String description = "Dear applicant, we want to notify you that you have been accepted " +
            "for new phase of the application, you are invited for an interview for this job.";
    private String link;
    private LocalDateTime dateTime;
    @ManyToOne
    private Job job;
    @OneToOne
    private Profile profile;
}

