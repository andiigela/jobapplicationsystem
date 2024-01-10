package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Follow;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.services.FollowService;
import com.ubt.andi.jobapp.services.JobService;
import com.ubt.andi.jobapp.services.ProfileService;
import com.ubt.andi.jobapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserService userService;
    private final FollowService followService;
    private static final int PAGE_SIZE = 10;
    public SearchController(ProfileService profileService,JobService jobService,
                            FollowService followService,UserService userService){
        this.profileService=profileService;
        this.jobService=jobService;
        this.followService=followService;
        this.userService=userService;
    }
    @GetMapping("/search")
    public String getSearchView(@RequestParam(value = "page",defaultValue = "0") String page,
                                @RequestParam(value = "searchButton") String searchValue,
                                @RequestParam(value = "searchKeyword") String searchKeyword,
                                Model model){

        if(searchKeyword != null && !searchKeyword.trim().equals("")){
            int pageNumber = Integer.parseInt(page);
            if(pageNumber > 0){
                pageNumber-=1;
            }
            Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
            AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            Profile followerProfile = user.getProfile();
            if(followerProfile == null) return "redirect:/profile/view/"+user.getUsername();
            if(searchKeyword.equals("Profile")){
                Page<Profile> profilePage = profileService.findProfileBySearch(searchValue,pageable);
                if(profilePage == null) return "redirect:/";
                for(Profile followingProfile : profilePage){
                    Follow follow = followService.existingFollow(followingProfile,followerProfile);
                    if(follow == null){
                        followingProfile.setFollowedByLoggedInUser(false);
                    }
                }
                model.addAttribute("searchedProfiles",profilePage);
                model.addAttribute("searchKeyword",searchKeyword);
                model.addAttribute("searchButton",searchValue);
                model.addAttribute("profile",followerProfile);
                return "index_withoutsearch";
            }
            if(searchKeyword.equals("Job")){
                Page<Job> jobPage = jobService.getAllJobsByTitle(searchValue,pageable);
                model.addAttribute("searchedJobs",jobPage);
                model.addAttribute("searchKeyword",searchKeyword);
                model.addAttribute("searchButton",searchValue);
                model.addAttribute("profile",followerProfile);
                return "index_withoutsearch";
            }
        }
        model.addAttribute("searchedProfiles",new ArrayList<Profile>());
        return "redirect:/search?searchButton="+searchValue+"&searchKeyword="+"Profile";
    }
}
