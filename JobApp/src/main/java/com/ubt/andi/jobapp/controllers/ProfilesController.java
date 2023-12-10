package com.ubt.andi.jobapp.controllers;

import com.ubt.andi.jobapp.dto.UserDto;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.services.ProfileService;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfilesController {
    private final UserService appUserService;
    private final ProfileService profileService;
    public ProfilesController(UserService appUserService,ProfileService profileService){
        this.appUserService=appUserService;
        this.profileService=profileService;
    }
    @GetMapping("/profile/view/{username}")
    public String getProfileeView(Model model,@PathVariable("username") String username){
        AppUser user = appUserService.findUserByUsername(username);
        Profile profile = null;
        if(user.getProfile() == null){
            profile = new Profile();
            profileService.createProfile(profile,user);
        } else {
            profile = user.getProfile();
        }
        model.addAttribute("profile",profile);
        return "profile";
    }
    @GetMapping("/profile/edit/{username}")
    public String editProfileeView(@PathVariable("username") String username,Model model) {
        AppUser user = appUserService.findUserByUsername(username);
        AppUser loggedInUser = appUserService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user != loggedInUser){
            return "redirect:/profile/edit/"+loggedInUser.getUsername();
        }
        Profile userProfile = user.getProfile();
        model.addAttribute("editProfile",userProfile);
        return "edit-profile";
    }
    @PostMapping("/profile/edit")
    public String editProfilee(@ModelAttribute("editProfile") Profile profile){
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        profileService.updateProfile(profile);
        return "redirect:/profile/view/" + authUser.getName();
    }

    @GetMapping("/profile/settings/{username}")
    public String getProfileView(@PathVariable("username") String username, Model model){
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if(!username.equals(authUser.getName())){
            return "redirect:/profile/settings/" + authUser.getName(); // nese /profile/{username} qit username e ka jep te ni acc qe sosht logged in, e kthen mrapa.
        }
        AppUser userDb = appUserService.findUserByUsername(username);
        if(userDb == null) return "redirect:/";
        model.addAttribute("editUser",userDb);
        return "edit-settings";
    }
    @PostMapping("/profile/settings")
    public String editProfile(@ModelAttribute("editUser") UserDto userDto){
        if(userDto == null) return "redirect:/";
        appUserService.updateUser(userDto);
        return "redirect:/logout";
    }
}
