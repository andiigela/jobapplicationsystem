package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Interview;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewService {
    void createInterview(Interview interview, Job job, Profile applicantProfile);
    Interview findInterviewById(Long id);
    Interview findInterviewByProfileAndJob(Profile profile, Job job);
    Page<Interview> findInterviewsByUser(Pageable pageable);
}
