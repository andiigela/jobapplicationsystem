package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.models.Share;
import com.ubt.andi.jobapp.repositories.PostRepository;
import com.ubt.andi.jobapp.repositories.ShareRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final ShareRepository shareRepository;

    public PostServiceImpl(PostRepository postRepository,UserRepository userRepository,ShareRepository shareRepository){
        this.postRepository=postRepository;
        this.userRepository=userRepository;
        this.shareRepository=shareRepository;
    }
    @Override
    public Page<Post> getPostsByPage(Pageable pageable) {
        if(pageable == null) return null;
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Page<Post> getPostsByUserId(Pageable pageable) {
        if(pageable == null) return null;
        AppUser appUser = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return postRepository.findPostsByAppUser_IdOrderByCreatedAtDesc(appUser.getId(),pageable);
    }

    @Override
    public void createPost(Post post) {
        if(post == null) return;
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        post.setAppUser(user);
        user.getPosts().add(post);
        postRepository.save(post);
    }

    @Override
    public Post getPostByIdAndUser(Long id) {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(id == 0 || user == null) return null;
        return postRepository.findPostByIdAndAppUser(id,user);
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    @Override
    public void editPost(Post post) {
        if (post != null) {
            Post postDb = postRepository.findById(post.getId()).orElse(null); // Retrieve post from the database

            if (postDb != null) {
                postDb.setDescription(post.getDescription());
                postDb.setJob(post.getJob());  // Make sure postDb is not null before setting properties
                // Set other properties as needed
                postRepository.save(postDb); // Save the updated post
            }
        }
    }


    @Override
    public void deletePost(Long id) {
        if(id == 0 || id == null) return;
        postRepository.deleteById(id);
    }



}
