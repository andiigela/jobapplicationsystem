package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Comment;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.repositories.CommentRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    public CommentServiceImpl(CommentRepository commentRepository,UserRepository userRepository){
        this.commentRepository=commentRepository;
        this.userRepository=userRepository;
    }
    @Override
    public Comment findCommentById(Long id) {
        if(id == 0) return null;
        return commentRepository.findById(id).get();
    }
    @Override
    public void createComment(Comment comment, Post post) {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(comment == null || post == null || user == null) return;
        comment.setPost(post);
        comment.setAppUser(user);
        commentRepository.save(comment);
    }
    @Override
    public void editComment(Comment comment) {
        if(comment == null) return;
        Comment commentDb = commentRepository.findById(comment.getId()).get();
        commentDb.setDescription(comment.getDescription());
        commentRepository.save(commentDb);
    }
    @Override
    public void deleteComment(Long commentId) {
        if(commentId == 0) return;
        commentRepository.deleteById(commentId);
    }
}
