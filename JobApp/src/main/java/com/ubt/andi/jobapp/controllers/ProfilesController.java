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
    @GetMapping("/profilee/view/{username}")
    public String getProfileeView(Model model,@PathVariable("username") String username){
        AppUser user = appUserService.findUserByUsername(username);
        Profile profile = null;
        if(user.getProfile() == null){
            profile = new Profile();
            user.setProfile(profile);
            profile.setFirstName("Anonymous");
            profile.setLastName("Anonymous");
            profile.setDescription("No Description");
            profile.setEducation("No Education");
            profile.setExperience("No Experience");
            profile.setProfession("No Profession");
            profile.setPhone("+383 45 000 000");
            profile.setExperienceLevel("No Experience Level");
            profile.setHourlyRate(0);
            profile.setTotalProjects(0);
            profile.setEnglishLevel("No English Level");
            profile.setGithubLink("No github link");
            profile.setSkills("No skills");
            profile.setAppuser(user);
            profileService.createProfile(profile);
        } else {
            profile = user.getProfile();
        }
        model.addAttribute("profile",profile);
        return "profile";
    }

    @GetMapping("/profile/{username}")
    public String getProfileView(@PathVariable("username") String username, Model model){
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if(!username.equals(authUser.getName())){
            return "redirect:/dashboard"; // nese /profile/{username} qit username e ka jep te ni acc qe sosht logged in, e kthen mrapa.
        }
        AppUser userDb = appUserService.findUserByUsername(username);
        if(userDb == null) return "redirect:/";
        model.addAttribute("editUser",userDb);
        return "edit-profile";
    }
    @PostMapping("/profile")
    public String editProfile(@ModelAttribute("editUser") UserDto userDto){
        if(userDto == null) return "redirect:/";
        appUserService.updateUser(userDto);
        return "redirect:/logout";
    }
}
