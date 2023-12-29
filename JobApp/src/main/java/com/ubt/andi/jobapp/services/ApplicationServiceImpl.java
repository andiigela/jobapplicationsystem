package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Application;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.repositories.ApplicationRepository;
import org.springframework.stereotype.Service;
@Service
public class ApplicationServiceImpl implements ApplicationService{
    private final ApplicationRepository applicationRepository;
    public ApplicationServiceImpl(ApplicationRepository applicationRepository){
        this.applicationRepository=applicationRepository;
    }
    @Override
    public void createApplication(Application application) {
        if(application == null) return;
        this.applicationRepository.save(application);
    }

    @Override
    public Application findApplicationByUserAndJob(AppUser user, Job job) {
        if(user == null || job == null) return null;
        return applicationRepository.findByAppUserAndJob(user,job);
    }
}
