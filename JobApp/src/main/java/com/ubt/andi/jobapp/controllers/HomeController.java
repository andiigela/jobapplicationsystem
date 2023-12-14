package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.services.JobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private JobService jobService;
    private static final int PAGE_SIZE = 5;
    public HomeController(JobService jobService){
        this.jobService=jobService;
    }
    @GetMapping("/")
    public String getHomeView(@RequestParam(value = "page",defaultValue = "0") String page, Model model){
        Pageable pageable = PageRequest.of(Integer.parseInt(page),PAGE_SIZE);
        Page<Job> jobs = jobService.getJobsByPage(pageable);
        model.addAttribute("jobs",jobs);
        return "home";
    }
}
