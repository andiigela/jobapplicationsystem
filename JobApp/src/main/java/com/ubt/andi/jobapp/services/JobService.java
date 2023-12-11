package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Job;

import java.util.List;

public interface JobService {
    List<Job> getJobs();
    void createJob(Job job);
    Job getJob(Long id);
    void deleteJobById(Long id);
    void editJob(Job job);
    List<Job> getAllJobsByTitle(String title);
}
