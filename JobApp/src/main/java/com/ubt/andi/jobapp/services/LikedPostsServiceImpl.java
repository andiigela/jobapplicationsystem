package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.LikedPosts;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.repositories.LikedPostsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikedPostsServiceImpl implements LikedPostsService{
    private final LikedPostsRepository likedPostsRepository;
    public LikedPostsServiceImpl(LikedPostsRepository likedPostsRepository){
        this.likedPostsRepository=likedPostsRepository;
    }

    @Override
    public void createLikedPost(LikedPosts likedPosts) {
        if(likedPosts == null) return;
        likedPostsRepository.save(likedPosts);
    }

    @Override
    public List<LikedPosts> findLikedPostsByUser(AppUser user) {
        if(user == null) return null;
        return likedPostsRepository.findByAppUser(user);
    }

    @Override
    public LikedPosts isPostLikedByUser(AppUser user,Post post) {
        if(post == null || user == null) return null;
        LikedPosts likedPosts = likedPostsRepository.findByAppUserAndPost(user,post);
        if(likedPosts != null){
            return likedPosts;
        }
        return null;
    }

    @Override
    public void deleteLikedPost(LikedPosts likedPosts) {
        if(likedPosts == null) return;
        likedPostsRepository.delete(likedPosts);
    }
}
