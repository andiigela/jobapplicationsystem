package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share,Long> {
    Share findShareByIdAndAppUser(Long id , AppUser u);
}
