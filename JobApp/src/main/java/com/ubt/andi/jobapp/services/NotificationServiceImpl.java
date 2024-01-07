package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Notification;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.repositories.NotificationRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    public NotificationServiceImpl(NotificationRepository notificationRepository,UserRepository userRepository){
        this.notificationRepository=notificationRepository;
        this.userRepository=userRepository;
    }
    @Override
    public void sendJobNotification(Profile toProfile,Job job) {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(toProfile == null || user == null || profile == null) return;
        Notification notification = new Notification();
        notification.setNotificationText("Congratulations "+toProfile.getFirstName() + " " + toProfile.getLastName() + " you have been accepted for this job " + job.getTitle());
        notification.setFromProfile(profile);
        notification.setToProfile(toProfile);
        notificationRepository.save(notification);
    }
    @Override
    public List<Notification> findNotifications() {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(profile == null || user == null) return null;
        return notificationRepository.findNotificationsByToProfile(profile);
    }
}
