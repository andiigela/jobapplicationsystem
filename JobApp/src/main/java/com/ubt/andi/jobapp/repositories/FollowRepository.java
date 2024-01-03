package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long> {
}
