package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.repositories.PostRepository;
import com.ubt.andi.jobapp.repositories.ShareRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository,UserRepository userRepository,ShareRepository shareRepository){
        this.postRepository=postRepository;
        this.userRepository=userRepository;
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
    public Post getPostById(Long id) {
        if(id == 0) return null;
        return postRepository.findById(id).get();
    }

    @Override
    public void editPost(Post post) {
        if(post == null) return;
        Post postDb = postRepository.findById(post.getId()).orElse(null);

        try {
            postDb.setDescription(post.getDescription());
        }catch (NullPointerException e){
            e.getMessage();
        }
        postDb.setJob(post.getJob());
        postRepository.save(postDb);
    }

    @Override
    public void deletePost(Long id) {
        if(id == 0 || id == null) return;
        postRepository.deleteById(id);
    }
}
