package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Application;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.services.ApplicationService;
import com.ubt.andi.jobapp.services.FileUploadService;
import com.ubt.andi.jobapp.services.JobService;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ApplicationsController {
    private final ApplicationService applicationService;
    private final UserService userService;
    private final JobService jobService;
    private final FileUploadService fileUploadService;
    public ApplicationsController(ApplicationService applicationService,UserService userService,
                                  JobService jobService,FileUploadService fileUploadService){
        this.applicationService=applicationService;
        this.userService=userService;
        this.jobService=jobService;
        this.fileUploadService=fileUploadService;
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
    public String sendApplication(@PathVariable("jobId") Long jobId, @RequestParam("cvUpload") MultipartFile cvUpload, @ModelAttribute("application") Application application){
        Job jobDb = jobService.getJob(jobId);
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Application existingApplication = applicationService.findApplicationByUserAndJob(user, jobDb);
        if(existingApplication != null){
            return "redirect:/job/"+jobId+"/apply";
        }
        application.setJob(jobDb);
        application.setAppUser(user);
        fileUploadService.saveDocument(application,cvUpload);
        applicationService.createApplication(application);
        return "redirect:/dashboard";
    }
    @PostMapping("/job/{jobId}/application/delete")
    public String deleteApplication(@PathVariable("jobId") Long jobId){
        AppUser userDb = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Job jobDb = jobService.getJob(jobId);
        Application applicationDb = applicationService.findApplicationByUserAndJob(userDb,jobDb);
        fileUploadService.deleteDocument(applicationDb);
        applicationService.deleteApplication(applicationDb);
        return "redirect:/job/"+jobId+"/apply";
    }
}
