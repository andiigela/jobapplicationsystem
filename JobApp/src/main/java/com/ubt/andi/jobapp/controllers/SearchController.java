package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.services.ProfileService;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    private final ProfileService profileService;
    public SearchController(ProfileService profileService){
        this.profileService=profileService;
    }
    @GetMapping("/search")
    public String getSearchView(@RequestParam("searchButton") String searchValue, Model model){
        List<Profile> profileBySearch = profileService.findProfileBySearch(searchValue);
        model.addAttribute("searchedProfiles",profileBySearch);
        return "search";
    }
}
