package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProfileService {
    void createProfile(Profile profile, AppUser user);
    Profile getProfileById(Long id);
    void updateProfile(Profile profile);
    Page<Profile> findProfileBySearch(String keyword, Pageable pageable);
}
