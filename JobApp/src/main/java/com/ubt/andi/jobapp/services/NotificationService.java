package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Notification;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    void sendAcceptedApplicantNotification(Profile toProfile, Job job);
    void sendNotAcceptedApplicantNotification(Profile toProfile, Job job);
    void sendFollowUserNotification(Profile toProfile);
    void sendPostNotification(Profile toProfile,Post post, String action);
    Page<Notification> findNotifications(Pageable pageable);
    void deleteNotification(Long id);
}
