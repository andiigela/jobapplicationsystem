package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.Notification;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.services.NotificationService;
import com.ubt.andi.jobapp.services.ProfileService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Controller
public class NotificationsController {
    private final NotificationService notificationService;
    private final ProfileService profileService;
    private final static int PAGE_SIZE = 5;
    public NotificationsController(NotificationService notificationService,ProfileService profileService){
        this.notificationService=notificationService;
        this.profileService=profileService;
    }
    @GetMapping("/notifications")
    public String getUserNotifications(@RequestParam(value = "page",defaultValue = "0") String page,Model model){
        int pageNumber = Integer.parseInt(page);
        if(pageNumber > 0){
            pageNumber-=1;
        }
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        Page<Notification> notifications = this.notificationService.findNotifications(pageable);
        model.addAttribute("notifications",notifications);
        return "notifications";
    }
}
