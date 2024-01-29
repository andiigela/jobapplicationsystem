package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.LikedPosts;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.models.Share;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share,Long> {
    Share findShareByIdAndAppUser(Long id , AppUser u);
    Share findByAppUserAndPost(AppUser user, Post post);
}
