package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Comment;
import com.ubt.andi.jobapp.models.Post;

public interface CommentService {
    Comment findCommentById(Long id);
    void createComment(Comment comment, Post post);
}
