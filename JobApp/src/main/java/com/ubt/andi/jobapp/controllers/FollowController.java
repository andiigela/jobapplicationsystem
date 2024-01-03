package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Follow;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.services.FollowService;
import com.ubt.andi.jobapp.services.ProfileService;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FollowController {
    private final FollowService followService;
    private final ProfileService profileService;
    private final UserService userService;
    public FollowController(FollowService followService,ProfileService profileService,UserService userService){
        this.followService=followService;
        this.profileService=profileService;
        this.userService=userService;
    }
    @PostMapping("/profile/{id}/follow")
    public String followProfile(@PathVariable("id") Long followingProfileId){
        Profile followingProfile = profileService.getProfileById(followingProfileId);
        AppUser loggedInUser = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile followerProfile = loggedInUser.getProfile();
        Follow follow = new Follow();
        follow.setFollowing(followingProfile);
        follow.setFollower(followerProfile);

        return "";
    }
}
