package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.Follow;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.repositories.FollowRepository;
import org.springframework.stereotype.Service;
@Service
public class FollowServiceImpl implements FollowService{
    private final FollowRepository followRepository;
    public FollowServiceImpl(FollowRepository followRepository){
        this.followRepository=followRepository;
    }
    @Override
    public void createFollow(Profile followingProfile, Profile followerProfile) {
        if(followingProfile == null || followerProfile == null) return;
        Follow follow = new Follow();
        follow.setFollowing(followingProfile);
        follow.setFollower(followerProfile);
        followRepository.save(follow);
    }

    @Override
    public void deleteFollow(Follow follow) {
        if(follow == null) return;
        followRepository.deleteById(follow.getId());
    }

    @Override
    public Follow existingFollow(Profile following, Profile follower) {
        if(following == null || follower == null) return null;
        return followRepository.findFollowByFollowingAndFollower(following,follower);
    }
}
