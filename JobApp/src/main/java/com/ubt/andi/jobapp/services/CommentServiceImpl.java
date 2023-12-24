package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.Comment;
import com.ubt.andi.jobapp.repositories.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository=commentRepository;
    }
    @Override
    public Comment findCommentById(Long id) {
        if(id == 0) return null;
        return commentRepository.findById(id).get();
    }
}
