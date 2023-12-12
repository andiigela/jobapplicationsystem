package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.services.JobService;
import com.ubt.andi.jobapp.services.ProfileService;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    private final ProfileService profileService;
    private final JobService jobService;
    private static final int PAGE_SIZE = 10;
    public SearchController(ProfileService profileService,JobService jobService){
        this.profileService=profileService;
        this.jobService=jobService;
    }
    @GetMapping("/search")
    public String getSearchView(@RequestParam(value = "page",defaultValue = "0") String page,
                                @RequestParam(value = "searchButton") String searchValue,Model model){//@RequestParam("searchButton") String searchValue,@RequestParam("searchKeyword") String searchKeyword,Model model
//        if(searchKeyword != null && !searchKeyword.trim().equals("")){
//            if(searchKeyword.equals("Profile")){
//                List<Profile> profilesBySearch = profileService.findProfileBySearch(searchValue);
//                model.addAttribute("searchedProfiles",profilesBySearch);
//                model.addAttribute("searchKeyword",searchKeyword);
//                return "search";
//            }
//            if(searchKeyword.equals("Job")){
//                List<Job> jobsBySearch = jobService.getAllJobsByTitle(searchValue);
//                model.addAttribute("searchedJobs",jobsBySearch);
//                model.addAttribute("searchKeyword",searchKeyword);
//                return "search";
//            }
//        }
//        model.addAttribute("searchedProfiles",new ArrayList<Profile>());
//        return "redirect:/search?searchButton="+searchValue+"&searchKeyword="+"Profile";
        int pageNumber = Integer.parseInt(page);
        if(pageNumber > 0){
            pageNumber-=1;
        }
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Job> jobPage = jobService.getAllJobsByTitle(searchValue,pageable);

        model.addAttribute("profilePage", jobPage);
        model.addAttribute("currentPage", Integer.parseInt(page));
        model.addAttribute("keyword",searchValue);
        return "search";
    }
}
