package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Application;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.services.ApplicationService;
import com.ubt.andi.jobapp.services.JobService;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class ApplicationsController {
    private final ApplicationService applicationService;
    private final UserService userService;
    private final JobService jobService;
    public ApplicationsController(ApplicationService applicationService,UserService userService,JobService jobService){
        this.applicationService=applicationService;
        this.userService=userService;
        this.jobService=jobService;
    }
    @GetMapping("/job/{jobId}/apply")
    public String getApplicationForm(@PathVariable("jobId") Long jobId,Model model){
        Job jobDb = jobService.getJob(jobId);
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Application existingApplication = applicationService.findApplicationByUserAndJob(user, jobDb);
        if(existingApplication == null){
            Application application = new Application();
            model.addAttribute("application",application);
        }
        model.addAttribute("user",user);
        model.addAttribute("existingApplication",existingApplication);
        model.addAttribute("job", jobDb);
        return "application-form";
    }
    @PostMapping("/job/{jobId}/apply")
    public String sendApplication(@PathVariable("jobId") Long jobId,@ModelAttribute("application") Application application){
        Job jobDb = jobService.getJob(jobId);
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Application existingApplication = applicationService.findApplicationByUserAndJob(user, jobDb);
        if(existingApplication != null){
            return "redirect:/job/"+jobId+"/apply";
        }
        application.setJob(jobDb);
        application.setAppUser(user);
        applicationService.createApplication(application);
        return "redirect:/dashboard";
    }
}
