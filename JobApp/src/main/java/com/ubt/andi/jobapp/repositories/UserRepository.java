package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser,Long> {
}
