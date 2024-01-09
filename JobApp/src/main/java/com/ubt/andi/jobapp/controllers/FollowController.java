package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Follow;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.services.FollowService;
import com.ubt.andi.jobapp.services.NotificationService;
import com.ubt.andi.jobapp.services.ProfileService;
import com.ubt.andi.jobapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FollowController {
    private final FollowService followService;
    private final ProfileService profileService;
    private final UserService userService;
    private final NotificationService notificationService;
    public FollowController(FollowService followService,ProfileService profileService,
                            UserService userService,NotificationService notificationService){
        this.followService=followService;
        this.profileService=profileService;
        this.userService=userService;
        this.notificationService=notificationService;
    }
    @GetMapping("/profile/{id}/follow")
    public String followProfile(@PathVariable("id") Long followingProfileId, HttpServletRequest request){
        Profile followingProfile = profileService.getProfileById(followingProfileId);
        AppUser loggedInUser = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile followerProfile = loggedInUser.getProfile();
        String referrer = request.getHeader("referer");
//        if(referrer != null && referrer.contains("/posts/" + postId + "/comment")){
//            return "redirect:/posts/" + postId + "/comment";
//        }
        if(followingProfile.equals(followerProfile)){
            return "redirect:redirect:/search?searchButton="+followingProfile.getFirstName()+"+"+followingProfile.getLastName()+"searchKeyword=Profile";
        }
        Follow existingFollow = followService.existingFollow(followingProfile,followerProfile);
        if(existingFollow != null){
            followService.deleteFollow(existingFollow);
            followingProfile.setFollowedByLoggedInUser(false);
            if(followerProfile.getFollowingsNumber() != 0){
                followerProfile.setFollowingsNumber(followerProfile.getFollowingsNumber()-1);
            }
            if(followingProfile.getFollowersNumber() != 0){
                followingProfile.setFollowersNumber(followingProfile.getFollowersNumber()-1);
            }
        } else {
            followService.createFollow(followingProfile,followerProfile);
            followingProfile.setFollowedByLoggedInUser(true);
            followerProfile.setFollowingsNumber(followerProfile.getFollowingsNumber()+1);
            followingProfile.setFollowersNumber(followingProfile.getFollowersNumber()+1);
            notificationService.sendFollowUserNotification(followingProfile);
        }
        profileService.updateProfile(followingProfile);
        profileService.updateProfile(followerProfile);
        return "redirect:redirect:/search?searchButton="+followingProfile.getFirstName()+"+"+followingProfile.getLastName()+"searchKeyword=Profile";
    }
}
