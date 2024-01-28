package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.models.Share;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Page<Post> getPostsByPage(Pageable pageable);
    Page<Post> getPostsByUserId(Pageable pageable);
    void createPost(Post post);
    Post getPostByIdAndUser(Long id);
    Post getPostById(Long id);
    void editPost(Post post);
    void deletePost(Long id);



}
