package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.repositories.JobRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    public JobServiceImpl(JobRepository jobRepository,UserRepository userRepository){
        this.jobRepository=jobRepository;
        this.userRepository=userRepository;
    }
    @Override
    public Page<Job> getJobsByUser(Pageable pageable) {
        if(pageable == null) return null;
        AppUser appUser = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return jobRepository.findAllByAppUser_Id(appUser.getId(),pageable);
    }

    @Override
    public void createJob(Job job) {
        if(job == null) return;
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        job.setAppUser(user);
        user.getJobs().add(job);
        jobRepository.save(job);
    }

    @Override
    public Job getJob(Long id) {
        if(id == 0 || id == null) return null;
        return jobRepository.findById(id).get();
    }

    @Override
    public void editJob(Job job) {
        if(job == null) return;
        Job jobDb = jobRepository.findById(job.getId()).get();
        jobDb.setTitle(job.getTitle());
        jobDb.setDescription(job.getDescription());
        jobDb.setLocation(job.getLocation());
        jobDb.setExpirationDate(job.getExpirationDate());
        jobRepository.save(jobDb);
    }

    @Override
    public Page<Job> getAllJobsByTitle(String title, Pageable pageable) {
        if(title.trim().equals("") || title == null) return null;
        return jobRepository.findJobsByTitleContainingIgnoreCase(title,pageable);
    }

    @Override
    public Page<Job> getJobsByPage(Pageable pageable) {
        if(pageable == null) return null;
        return jobRepository.findAll(pageable);
    }

    @Override
    public void deleteJobById(Long id) {
        if(id == 0 || id == null) return;
        jobRepository.deleteById(id);
    }

}
