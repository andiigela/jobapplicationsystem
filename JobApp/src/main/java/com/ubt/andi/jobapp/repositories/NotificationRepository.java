package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.Notification;
import com.ubt.andi.jobapp.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findNotificationsByToProfile(Profile profile);
}
