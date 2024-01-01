package com.ubt.andi.jobapp.controllers;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Application;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.services.ApplicationService;
import com.ubt.andi.jobapp.services.JobService;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class JobsController {
    private final JobService jobService;
    private final UserService userService;
    private final ApplicationService applicationService;
    private final static int PAGE_SIZE = 5;
    public JobsController(JobService jobService,ApplicationService applicationService,UserService userService){
        this.jobService=jobService;
        this.applicationService=applicationService;
        this.userService=userService;
    }
    @GetMapping("/jobs")
    public String getJobsView(@RequestParam(value = "page",defaultValue = "0") String page, Model model){
        int pageNumber = Integer.parseInt(page);
        if(pageNumber > 0){
            pageNumber-=1;
        }
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        Page<Job> retrieveJobs = jobService.getJobsByUser(pageable);
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
        return "redirect:/jobs";
    }
    @PostMapping("/jobs/delete/{id}")
    public String deleteJob(@PathVariable("id") Long id){
        jobService.deleteJobById(id);
        return "redirect:/jobs";
    }
    @GetMapping("/job/{jobId}/applicants")
    public String getApplicantsView(@RequestParam(value = "page",defaultValue = "0") String page,
                                    @PathVariable("jobId") Long jobId,Model model){
        int pageNumber = Integer.parseInt(page);
        if(pageNumber > 0){
            pageNumber-=1;
        }
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        Job job = jobService.getJob(jobId);
        Page<Application> retrieveApplications = applicationService.findApplicationsByCreationDateDesc(job,pageable);
        model.addAttribute("apps",retrieveApplications);
        model.addAttribute("job",job);
        return "show-applicants";
    }
}
