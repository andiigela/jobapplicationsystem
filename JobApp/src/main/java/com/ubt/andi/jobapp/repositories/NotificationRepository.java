package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.Notification;
import com.ubt.andi.jobapp.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findNotificationsByToProfile(Profile profile, Pageable pageable);
}
