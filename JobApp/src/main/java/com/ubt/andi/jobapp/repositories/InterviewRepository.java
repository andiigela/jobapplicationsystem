package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.Interview;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview,Long> {
    Interview findByProfileAndJob(Profile profile, Job job);
    Page<Interview> findInterviewsByProfile(Profile profile, Pageable pageable);
}
