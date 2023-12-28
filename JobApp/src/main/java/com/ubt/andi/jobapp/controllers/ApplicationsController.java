package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.services.ApplicationService;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationsController {
    private final ApplicationService applicationService;
    private final UserService userService;
    public ApplicationsController(ApplicationService applicationService,UserService userService){
        this.applicationService=applicationService;
        this.userService=userService;
    }
    @GetMapping("/job/apply")
    public String getApplicationForm(Model model){
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("user",user);
        return "application-form";
    }
}
