package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Profile;

public interface NotificationService {
    void sendJobNotification(Profile toProfile, Job job);
}
