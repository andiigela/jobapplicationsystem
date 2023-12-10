package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Profile;

import java.util.List;

public interface ProfileService {
    void createProfile(Profile profile, AppUser user);
    void updateProfile(Profile profile);
    List<Profile> findProfileBySearch(String keyword);
}
