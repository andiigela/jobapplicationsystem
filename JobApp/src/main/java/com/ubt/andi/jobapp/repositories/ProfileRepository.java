package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
}
