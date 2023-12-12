package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Page<Profile> findProfilesByFirstNameContainingAndLastNameContaining(String firstName, String Lastname, Pageable pageable);
}
