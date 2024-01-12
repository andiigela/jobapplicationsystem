package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Application;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Notification;
import com.ubt.andi.jobapp.repositories.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        application.setApproved(false);
        this.applicationRepository.save(application);
    }

    @Override
    public Application findApplicationByUserAndJob(AppUser user, Job job) {
        if(user == null || job == null) return null;
        return applicationRepository.findByAppUserAndJob(user,job);
    }

    @Override
    public Application findApplicationById(Long id) {
        if(id == 0) return null;
        return applicationRepository.findById(id).get();
    }

    @Override
    public void deleteApplication(Application application) {
        if(application == null) return;
        applicationRepository.delete(application);
    }

    @Override
    public void editApplication(Application application) {
        if(application == null) return;
        Application appDb = applicationRepository.findById(application.getId()).get();
        appDb.setApproved(application.isApproved());
        applicationRepository.save(appDb);
    }

    @Override
    public Page<Application> findApplicationsByCreationDateDesc(Job job,Pageable pageable) {
        if(pageable == null || job == null) return null;
        return applicationRepository.findAllByJobOrderByCreationDateDesc(job,pageable);
    }
}
