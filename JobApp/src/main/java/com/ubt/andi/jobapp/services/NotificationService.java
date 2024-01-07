package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Notification;
import com.ubt.andi.jobapp.models.Profile;

import java.util.List;

public interface NotificationService {
    void sendJobNotification(Profile toProfile, Job job);
    List<Notification> findNotifications();
}
