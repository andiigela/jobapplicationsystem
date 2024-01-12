package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Notification;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.services.NotificationService;
import com.ubt.andi.jobapp.services.ProfileService;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Controller
public class NotificationsController {
    private final NotificationService notificationService;
    private final ProfileService profileService;
    private final UserService userService;
    private final static int PAGE_SIZE = 5;
    public NotificationsController(NotificationService notificationService,ProfileService profileService,UserService userService){
        this.notificationService=notificationService;
        this.profileService=profileService;
        this.userService=userService;
    }
    @GetMapping("/notifications")
    public String getUserNotifications(@RequestParam(value = "page",defaultValue = "0") String page,Model model){
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(profile == null) return "redirect:/profile/view"+user.getUsername();
        profile.setNotificationsNumber(0);
        profileService.updateProfile(profile);
        int pageNumber = Integer.parseInt(page);
        if(pageNumber > 0){
            pageNumber-=1;
        }
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        Page<Notification> notifications = this.notificationService.findNotifications(pageable);
        model.addAttribute("notifications",notifications);
        model.addAttribute("profile",profile);
        return "notifications";
    }
    @PostMapping("/notification/{id}/delete")
    public String deleteNotification(@PathVariable("id") Long id){
        notificationService.deleteNotification(id);
        return "redirect:/notifications";
    }
}
