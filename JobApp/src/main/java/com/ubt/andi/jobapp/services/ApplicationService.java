package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Application;
import com.ubt.andi.jobapp.models.Job;

public interface ApplicationService {
    void createApplication(Application application);
    Application findApplicationByUserAndJob(AppUser user, Job job);
}
