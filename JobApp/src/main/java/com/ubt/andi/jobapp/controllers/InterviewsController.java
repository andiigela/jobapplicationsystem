package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Interview;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Profile;
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
public class InterviewsController {
    private final InterviewService interviewService;
    private final UserService userService;
    private final JobService jobService;
    private final NotificationService notificationService;
    private final ProfileService profileService;
    public InterviewsController(InterviewService interviewService,UserService userService,
                                JobService jobService,NotificationService notificationService,
                                ProfileService profileService){
        this.interviewService=interviewService;
        this.userService=userService;
        this.jobService=jobService;
        this.notificationService=notificationService;
        this.profileService=profileService;
    }
    private final static int PAGE_SIZE = 5;
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
        if(interview.getProfile().getId() != profile.getId()) return "redirect:/";
        model.addAttribute("interview", interview);
        model.addAttribute("profile", profile);
        return "view-interview";
    }
    @GetMapping("/interviews")
    public String getInterviewsPage(@RequestParam(value = "page",defaultValue = "0") String page, Model model){
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(profile == null) return "redirect:/profile/view/"+user.getUsername();
        int pageNumber = Integer.parseInt(page);
        if(pageNumber > 0){
            pageNumber-=1;
        }
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        Page<Interview> interviews = interviewService.findInterviewsByUser(pageable);
        model.addAttribute("profile",profile);
        model.addAttribute("interviews",interviews);
        return "my-interviews";
    }

}
