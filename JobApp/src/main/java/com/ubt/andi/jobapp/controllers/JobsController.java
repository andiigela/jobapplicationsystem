package com.ubt.andi.jobapp.controllers;

import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.services.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class JobsController {
    private final JobService jobService;
    public JobsController(JobService jobService){
        this.jobService=jobService;
    }
    @GetMapping("/jobs")
    public String getJobsView(Model model){
        List<Job> retrieveJobs = jobService.getJobs();
        model.addAttribute("jobs",retrieveJobs);
        return "jobs";
    }
    @GetMapping("/jobs/create")
    public String getCreateJobView(Model model){
        model.addAttribute("job",new Job());
        return "create-job";
    }
    @PostMapping("/jobs/create")
    public String CreateJob(@ModelAttribute("job") Job job){
        jobService.createJob(job);
        return "redirect:/jobs";
    }
    @GetMapping("/jobs/edit/{id}")
    public String getEditJobView(@PathVariable("id") Long id,Model model){
        Job editJob = jobService.getJob(id);
        model.addAttribute("jobEdit",editJob);
        model.addAttribute("formattedDate",editJob.getExpirationDate().toString());
        return "edit-job";
    }
    @PostMapping("/jobs/edit")
    public String editJob(@ModelAttribute("jobEdit") Job job){
        jobService.editJob(job);
        return "redirect:/jobs/edit/"+job.getId();
    }
    @PostMapping("/jobs/delete/{id}")
    public String editJob(@PathVariable("id") Long id){
        jobService.deleteJobById(id);
        return "redirect:/jobs";
    }
}
