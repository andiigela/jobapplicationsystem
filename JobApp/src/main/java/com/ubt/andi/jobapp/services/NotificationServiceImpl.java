package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.*;
import com.ubt.andi.jobapp.repositories.NotificationRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ProfileService profileService;
    public NotificationServiceImpl(NotificationRepository notificationRepository,UserRepository userRepository,ProfileService profileService){
        this.notificationRepository=notificationRepository;
        this.userRepository=userRepository;
        this.profileService=profileService;
    }
    @Override
    public void sendAcceptedApplicantNotification(Profile toProfile,Job job) {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(toProfile == null || user == null || profile == null) return;
        Notification notification = new Notification();
        notification.setNotificationText("Congratulations "+toProfile.getFirstName() + " " + toProfile.getLastName() + " you have been accepted for this job: " + job.getTitle());
        notification.setFromProfile(profile);
        notification.setToProfile(toProfile);
        notificationRepository.save(notification);
        toProfile.setNotificationsNumber(toProfile.getNotificationsNumber()+1);
        profileService.updateProfile(toProfile);
    }
    @Override
    public void sendNotAcceptedApplicantNotification(Profile toProfile,Job job) {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(toProfile == null || user == null || profile == null) return;
        Notification notification = new Notification();
        notification.setNotificationText("Hey "+toProfile.getFirstName() + " " + toProfile.getLastName() + " we are truly sorry but you are not approved for this job: " + job.getTitle());
        notification.setFromProfile(profile);
        notification.setToProfile(toProfile);
        notificationRepository.save(notification);
        toProfile.setNotificationsNumber(toProfile.getNotificationsNumber()+1);
        profileService.updateProfile(profile);

    }

    @Override
    public void sendInterviewNotification(Profile toProfile, Job job,Interview interview) {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(toProfile == null || job == null || profile == null) return;
        Notification notification = new Notification();
        notification.setNotificationText("Dear Applicant "+toProfile.getFirstName() + " " + toProfile.getLastName()+ " we want to notify you about your job: " + job.getTitle() + " click here to see more.");
        notification.setFromProfile(profile);
        notification.setToProfile(toProfile);
        notificationRepository.save(notification);
        toProfile.setNotificationsNumber(toProfile.getNotificationsNumber()+1);
        profileService.updateProfile(profile);
    }

    @Override
    public void sendFollowUserNotification(Profile toProfile) {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(toProfile == null || user == null || profile == null) return;
        Notification notification = new Notification();
        notification.setNotificationText(user.getUsername() + " has followed you");
        notification.setToProfile(toProfile);
        notification.setFromProfile(profile);
        notificationRepository.save(notification);
        toProfile.setNotificationsNumber(toProfile.getNotificationsNumber()+1);
        profileService.updateProfile(profile);
    }

    @Override
    public void sendPostNotification(Profile toProfile,Post post, String action) {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(toProfile == null || user == null || profile == null) return;
        Notification notification = new Notification();
        if(action.trim().equals("Like")){
            notification.setNotificationText(profile.getFirstName() + " " + profile.getLastName() + " has liked your post: " + post.getDescription());
        }
        if(action.trim().equals("Comment")){
            notification.setNotificationText(profile.getFirstName() + " " + profile.getLastName() + " has commented on your post: " + post.getDescription());
        }
        if (action.trim().equals("Share")){
            notification.setNotificationText(profile.getFirstName()+" "+profile.getLastName() + " has shared your post " +post.getDescription());
        }
        notification.setToProfile(toProfile);
        notification.setFromProfile(profile);
        notificationRepository.save(notification);
        toProfile.setNotificationsNumber(toProfile.getNotificationsNumber()+1);
        profileService.updateProfile(profile);
    }

    @Override
    public Page<Notification> findNotifications(Pageable pageable) {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(profile == null || user == null || pageable == null) return null;
        return notificationRepository.findNotificationsByToProfileOrderByCreatedAtDesc(profile,pageable);
    }

    @Override
    public void deleteNotification(Long id) {
        if(id == 0) return;
        notificationRepository.deleteById(id);
    }
}
