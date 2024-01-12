package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.LikedPosts;
import com.ubt.andi.jobapp.models.Post;

import java.util.List;

public interface LikedPostsService {
    void createLikedPost(LikedPosts likedPosts);
    List<LikedPosts> findLikedPostsByUser(AppUser user);
    LikedPosts isPostLikedByUser(AppUser user,Post post);
    void deleteLikedPost(LikedPosts likedPosts);
}
