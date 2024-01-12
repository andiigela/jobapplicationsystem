package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    void sendAcceptedApplicantNotification(Profile toProfile, Job job);
    void sendNotAcceptedApplicantNotification(Profile toProfile, Job job);
    void sendInterviewNotification(Profile toProfile, Job job, Interview interview);
    void sendFollowUserNotification(Profile toProfile);
    void sendPostNotification(Profile toProfile,Post post, String action);
    Page<Notification> findNotifications(Pageable pageable);
    void deleteNotification(Long id);
}
