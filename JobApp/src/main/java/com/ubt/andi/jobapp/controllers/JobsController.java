package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.*;
import com.ubt.andi.jobapp.services.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class JobsController {
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final NotificationService notificationService;
    private final ProfileService profileService;
    private final UserService userService;
    private final InterviewService interviewService;
    private final static int PAGE_SIZE = 5;
    public JobsController(JobService jobService,ApplicationService applicationService,
                          NotificationService notificationService,UserService userService,
                          InterviewService interviewService,ProfileService profileService){
        this.jobService=jobService;
        this.applicationService=applicationService;
        this.notificationService=notificationService;
        this.userService=userService;
        this.interviewService=interviewService;
        this.profileService=profileService;
    }
    @GetMapping("/jobs")
    public String getJobsView(@RequestParam(value = "page",defaultValue = "0") String page, Model model){
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(profile == null) return "redirect:/profile/view/"+user.getUsername();
        int pageNumber = Integer.parseInt(page);
        if(pageNumber > 0){
            pageNumber-=1;
        }
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        Page<Job> retrieveJobs = jobService.getJobsByUser(pageable);
        model.addAttribute("jobs",retrieveJobs);
        model.addAttribute("profile",profile);
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
        Job editJob = jobService.getJobByIdAndUser(id); // user is inside this method
        if(editJob == null){
            return "redirect:/jobs";
        }
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
    @GetMapping("/job/{jobId}/details")
    public String getJobDetails(@PathVariable("jobId") Long jobId,Model model){
        Job job = jobService.getJobById(jobId);
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(profile == null) return "redirect:/profile/view/"+user.getUsername();
        if(!job.isActive()) return "redirect:/";
        model.addAttribute("job",job);
        model.addAttribute("profile",profile);
        return "job-details";
    }
    @GetMapping("/job/{jobId}/applicants")
    public String getApplicantsView(@RequestParam(value = "page",defaultValue = "0") String page,
                                    @PathVariable("jobId") Long jobId,Model model){
        int pageNumber = Integer.parseInt(page);
        if(pageNumber > 0){
            pageNumber-=1;
        }
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(profile == null) return "redirect:/profile/view/"+user.getUsername();
        Job job = jobService.getJobByIdAndUser(jobId);
        if(job == null){
            return "redirect:/jobs";
        }
        Page<Application> retrieveApplications = applicationService.findApplicationsByCreationDateDesc(job,pageable);
        model.addAttribute("apps",retrieveApplications);
        model.addAttribute("job",job);
        model.addAttribute("profile",profile);
        return "show-applicants";
    }
    @PostMapping("/job/{jobId}/applicants/application/{appId}/approve")
    public String approveApplication(@PathVariable("jobId") Long jobId,@PathVariable("appId") Long appId
            ,@RequestParam("approval") String approvalValue){
        Job job = jobService.getJobByIdAndUser(jobId);
        if(job == null){
            return "redirect:/jobs";
        }
        Application application = applicationService.findApplicationById(appId);
        if(approvalValue.trim().equals("1")){
            application.setApproved(true);
            notificationService.sendAcceptedApplicantNotification(application.getAppUser().getProfile(),job);

        }
        if(approvalValue.trim().equals("0")){
            application.setApproved(false);
            notificationService.sendNotAcceptedApplicantNotification(application.getAppUser().getProfile(),job);
        }
        applicationService.editApplication(application);
        return "redirect:/job/"+jobId+"/applicants";
    }
    @GetMapping("/job/{jobId}/applicants/{applicantId}/interview/create")
    public String getInterviewCreate(@PathVariable("jobId") Long jobId,@PathVariable("applicantId") Long applicantId,Model model){
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        model.addAttribute("interview", new Interview());
        model.addAttribute("profile", profile);
        model.addAttribute("jobId", jobId);
        model.addAttribute("applicantId", applicantId);
        return "create-interview";
    }
    @PostMapping("/job/{jobId}/applicants/{applicantId}/interview/create")
    public String createInterview(@PathVariable("jobId") Long jobId,@PathVariable("applicantId") Long applicantId,
                                  @ModelAttribute("interview") Interview interview){
        if(interview.getDateTime().isBefore(LocalDateTime.now())){
            return "redirect:/job/" + jobId + "/applicants/" + applicantId + "/interview/create";
        }
        Job job = jobService.getJobById(jobId);
        Profile applicantProfile = profileService.getProfileById(applicantId);
        interviewService.createInterview(interview,job,applicantProfile);
        notificationService.sendInterviewNotification(applicantProfile,job,interview);
        return "redirect:/job/"+jobId+"/applicants";
    }
    @GetMapping("/interview/{id}")
    public String viewInterviewByApplicant(@PathVariable("id") Long id, Model model){
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        Interview interview = interviewService.findInterviewById(id);
        model.addAttribute("interview", interview);
        model.addAttribute("profile", profile);
        return "view-interview";
    }
}
