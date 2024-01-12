package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.repositories.JobRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(job == null || user == null) return;
        LocalDate currentDate = LocalDate.now();
        if(job.getExpirationDate().isAfter(currentDate) || job.getExpirationDate().isEqual(currentDate)){
            job.setActive(true);
        } else {
            job.setActive(false);
        }
        job.setAppUser(user);
        user.getJobs().add(job);
        jobRepository.save(job);
    }
    @Override
    public Job getJobByIdAndUser(Long id) {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(id == 0 || user == null) return null;
        return jobRepository.findJobByIdAndAppUser(id,user);
    }

    @Override
    public Job getJobById(Long id) {
        if(id == 0) return null;
        return jobRepository.findById(id).get();
    }

    @Override
    public void editJob(Job job) {
        if(job == null) return;
        LocalDate currentDate = LocalDate.now();
        if(job.getExpirationDate().isAfter(currentDate) || job.getExpirationDate().isEqual(currentDate)){
            job.setActive(true);
        } else {
            job.setActive(false);
        }
        Job jobDb = jobRepository.findById(job.getId()).get();
        jobDb.setTitle(job.getTitle());
        jobDb.setDescription(job.getDescription());
        jobDb.setLocation(job.getLocation());
        jobDb.setActive(job.isActive());
        if(job.getExpirationDate() != null){
            jobDb.setExpirationDate(job.getExpirationDate());
        }
        jobDb.setSalary(job.getSalary());
        jobDb.setJobType(job.getJobType());
        jobDb.setCompanyName(job.getCompanyName());
        jobDb.setPosition(job.getPosition());
        jobDb.setFirstResponsibility(job.getFirstResponsibility());
        jobDb.setSecondResponsibility(job.getSecondResponsibility());
        jobDb.setThirdResponsibility(job.getThirdResponsibility());
        jobDb.setFirstRequirement(job.getFirstRequirement());
        jobDb.setSecondRequirement(job.getSecondRequirement());
        jobDb.setThirdRequirement(job.getThirdRequirement());
        jobRepository.save(jobDb);
    }
    @Override
    public Page<Job> getAllJobsByTitle(String title, Pageable pageable) {
        if(title.trim().equals("") || title == null) return null;
        return jobRepository.findJobsByTitleContainingIgnoreCaseAndActiveIsTrue(title,pageable);
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
