package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Comment;

public interface CommentService {
    Comment findCommentById(Long id);
}
