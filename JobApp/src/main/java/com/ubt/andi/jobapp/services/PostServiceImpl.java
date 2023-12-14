package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    public PostServiceImpl(PostRepository postRepository){
        this.postRepository=postRepository;
    }
    @Override
    public Page<Post> getPostsByPage(Pageable pageable) {
        return null;
    }

    @Override
    public void createPost(Post post) {

    }

}
