package com.ubt.andi.jobapp.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationsController {
    public NotificationsController(){

    }
    @GetMapping("/notifications")
    public String getUserNotifications(){
        return "notifications";
    }
}
