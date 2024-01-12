package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Follow;
import com.ubt.andi.jobapp.models.Profile;

public interface FollowService {
    void createFollow(Profile followingProfile, Profile followerProfile);
    void deleteFollow(Follow follow);
    Follow existingFollow(Profile following, Profile follower);
}
