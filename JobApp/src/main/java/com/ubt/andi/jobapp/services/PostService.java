package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<Post> getPostsByPage(Pageable pageable);
    Page<Post> getPostsByUserId(Pageable pageable);
    void createPost(Post post);
    Post getPostById(Long id);
    void editPost(Post post);
}
