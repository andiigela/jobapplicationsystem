package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.repositories.ProfileRepository;
import org.springframework.stereotype.Service;
@Service
public class ProfileServiceImpl implements ProfileService{
    private final ProfileRepository profileRepository;
    public ProfileServiceImpl(ProfileRepository profileRepository){
        this.profileRepository=profileRepository;
    }


    @Override
    public void createProfile(Profile profile) {
        if(profile == null) return;
        this.profileRepository.save(profile);
    }
}
