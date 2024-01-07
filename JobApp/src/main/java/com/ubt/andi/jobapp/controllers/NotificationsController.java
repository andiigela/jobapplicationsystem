package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.Notification;
import com.ubt.andi.jobapp.services.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationsController {
    private final NotificationService notificationService;
    public NotificationsController(NotificationService notificationService){
        this.notificationService=notificationService;
    }
    @GetMapping("/notifications")
    public String getUserNotifications(Model model){
        List<Notification> notifications = this.notificationService.findNotifications();
        model.addAttribute("notifications",notifications);
        return "notifications";
    }
}
