package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Interview;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Profile;

public interface InterviewService {
    void createInterview(Interview interview, Job job, Profile applicantProfile);
}
