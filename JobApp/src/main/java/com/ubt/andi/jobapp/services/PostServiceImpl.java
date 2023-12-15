package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.repositories.PostRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public PostServiceImpl(PostRepository postRepository,UserRepository userRepository){
        this.postRepository=postRepository;
        this.userRepository=userRepository;
    }
    @Override
    public Page<Post> getPostsByPage(Pageable pageable) {
        if(pageable == null) return null;
        return postRepository.findAllByOrderByUpdatedAtDesc(pageable);
    }

    @Override
    public Page<Post> getPostsByUserId(Pageable pageable) {
        if(pageable == null) return null;
        AppUser appUser = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return postRepository.findPostsByAppUser_IdOrderByUpdatedAtDesc(appUser.getId(),pageable);
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
    public Post getPostById(Long id) {
        if(id == 0 || id == null) return null;
        return postRepository.findById(id).get();
    }

    @Override
    public void editPost(Post post) {
        if(post == null) return;
        Post postDb = postRepository.findById(post.getId()).get();
        postDb.setDescription(post.getDescription());
        postRepository.save(postDb);
    }
}
