package com.ubt.andi.jobapp.controllers;

import com.ubt.andi.jobapp.dto.UserDto;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller()
public class ProfilesController {
    private final UserService appUserService;
    public ProfilesController(UserService appUserService){
        this.appUserService=appUserService;
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
        return "redirect:/dashboard";
    }
}
