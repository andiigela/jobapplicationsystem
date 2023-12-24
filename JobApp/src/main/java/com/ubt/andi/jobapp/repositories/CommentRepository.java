package com.ubt.andi.jobapp.repositories;
import com.ubt.andi.jobapp.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
