package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Application;
import com.ubt.andi.jobapp.models.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {
    void createApplication(Application application);
    Application findApplicationByUserAndJob(AppUser user, Job job);
    Application findApplicationById(Long id);
    void deleteApplication(Application application);
    void editApplication(Application application);
    Page<Application> findApplicationsByCreationDateDesc(Job job,Pageable pageable);
}
