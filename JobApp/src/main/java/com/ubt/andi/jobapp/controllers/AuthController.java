package com.ubt.andi.jobapp.controllers;

import com.ubt.andi.jobapp.dto.UserDto;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService){
        this.userService=userService;
    }
    @GetMapping("/register")
    public String getRegisterView(Model model){
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if(!(authUser instanceof AnonymousAuthenticationToken)){
            return "redirect:/dashboard";
        }
        model.addAttribute("user", new UserDto());
        return "register-form";
    }
    @PostMapping("/register")
    public String registerUsers(@ModelAttribute("user") UserDto userDto, @RequestParam("radioRoles") boolean radio){
        userDto.setRoleAccount(radio);
        this.userService.createUser(userDto);
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String getLoginView(Model model){
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if(!(authUser instanceof AnonymousAuthenticationToken)){
            return "redirect:/profile/view/" + authUser.getName();
        }
        model.addAttribute("user",new AppUser());
        return "login";
    }
    @GetMapping("/dashboard")
    public String getDashboardView(Model model){
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = userService.findUserByUsername(authUser.getName());
        model.addAttribute("user",authUser);
        return "dashboard";
    }
//    public String withoutAccess(String returnParam){
//        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
//        if(!(authUser instanceof AnonymousAuthenticationToken)){
//            return "redirect:/dashboard";
//        }
//        return returnParam;
//    }
}
