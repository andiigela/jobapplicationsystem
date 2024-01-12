package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.LikedPosts;
import com.ubt.andi.jobapp.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikedPostsRepository extends JpaRepository<LikedPosts,Long> {
    List<LikedPosts> findByAppUser(AppUser user);
    LikedPosts findByAppUserAndPost(AppUser user,Post post);
}
