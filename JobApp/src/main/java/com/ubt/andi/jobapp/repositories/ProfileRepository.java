package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    List<Profile> findProfilesByFirstNameContainingAndLastNameContaining(String firstName, String Lastname);
}
