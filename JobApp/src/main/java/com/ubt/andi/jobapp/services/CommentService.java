package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Comment;
import com.ubt.andi.jobapp.models.Post;

public interface CommentService {
    Comment findCommentById(Long id);
    Comment findCommentByIdAndUser(Long id);
    void createComment(Comment comment, Post post);
    void editComment(Comment comment);
    void deleteComment(Long commentId);
}
