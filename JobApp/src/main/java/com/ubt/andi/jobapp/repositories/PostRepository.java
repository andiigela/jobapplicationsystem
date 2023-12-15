package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByUpdatedAtDesc(Pageable pageable);
    Page<Post> findPostsByAppUser_Id(@Param("id") Long id, Pageable pageable);

}
