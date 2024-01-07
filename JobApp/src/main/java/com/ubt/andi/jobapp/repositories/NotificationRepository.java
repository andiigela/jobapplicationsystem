package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
