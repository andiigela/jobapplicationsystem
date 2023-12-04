package com.ubt.andi.jobapp.controllers;

import com.ubt.andi.jobapp.dto.UserDto;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @GetMapping("/register")
    public String getRegisterView(Model model){
        model.addAttribute("user", new UserDto());
        return "register-form";
    }
    @PostMapping("/register")
    public String registerUsers(@ModelAttribute("user") UserDto userDto){

        return "";
    }
}
