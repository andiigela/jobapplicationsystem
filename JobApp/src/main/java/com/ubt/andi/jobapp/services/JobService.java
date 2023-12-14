package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {
    Page<Job> getJobsByUser(Pageable pageable);
    void createJob(Job job);
    Job getJob(Long id);
    void deleteJobById(Long id);
    void editJob(Job job);
    Page<Job> getAllJobsByTitle(String title, Pageable pageable);
    Page<Job> getJobsByPage(Pageable pageable);
}
