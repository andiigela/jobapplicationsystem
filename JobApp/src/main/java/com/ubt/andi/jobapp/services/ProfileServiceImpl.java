package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.repositories.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService{
    private final ProfileRepository profileRepository;
    public ProfileServiceImpl(ProfileRepository profileRepository){
        this.profileRepository=profileRepository;
    }


    @Override
    public void createProfile(Profile profile, AppUser user) {
        if(profile == null) return;
        user.setProfile(profile);
        profile.setAppuser(user);
        profile.setFirstName("Anonymous");
        profile.setLastName("Anonymous");
        profile.setDescription("No Description");
        profile.setEducation("No Education");
        profile.setExperience("No Experience");
        profile.setProfession("No Profession");
        profile.setPhone("+383 45 000 000");
        profile.setExperienceLevel("No Experience Level");
        profile.setHourlyRate(0);
        profile.setTotalProjects(0);
        profile.setEnglishLevel("No English Level");
        profile.setGithubLink("No github link");
        profile.setSkills("No skills");
        profile.setImagePath("/static/images/anonymous.png");
        this.profileRepository.save(profile);
    }

    @Override
    public void updateProfile(Profile profile) {
        if(profile == null) return;
        Profile profileDb = this.profileRepository.findById(profile.getId()).get();
        profileDb.setId(profile.getId());
        profileDb.setFirstName(profile.getFirstName());
        profileDb.setLastName(profile.getLastName());
        profileDb.setDescription(profile.getDescription());
        profileDb.setEducation(profile.getEducation());
        profileDb.setExperience(profile.getExperience());
        profileDb.setProfession(profile.getProfession());
        profileDb.setPhone(profile.getPhone());
        profileDb.setExperienceLevel(profile.getExperienceLevel());
        profileDb.setHourlyRate(profile.getHourlyRate());
        profileDb.setTotalProjects(profile.getTotalProjects());
        profileDb.setEnglishLevel(profile.getEnglishLevel());
        profileDb.setGithubLink(profile.getGithubLink());
        profileDb.setSkills(profile.getSkills());
        profileDb.setImageData(profile.getImageData());
        profileDb.setImagePath(profile.getImagePath());
        profileRepository.save(profileDb);
    }

    @Override
    public Page<Profile> findProfileBySearch(String searchKeyword, Pageable pageable) {
        if(searchKeyword == null || searchKeyword.trim().equals("")) return null;
        String[] keywords = searchKeyword.split(" ");
        if(keywords.length == 1){
            return profileRepository.findProfilesByFirstNameContainingAndLastNameContaining(keywords[0], "",pageable);
        }
        if(keywords.length > 1){
            return profileRepository.findProfilesByFirstNameContainingAndLastNameContaining(keywords[0],keywords[keywords.length-1],pageable);
        }
        return null;
    }
}
