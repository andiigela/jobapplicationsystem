package com.ubt.andi.jobapp.repositories;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job,Long> {
    Page<Job> findAllByAppUser_Id(@Param("id") Long id,Pageable pageable);
    Page<Job> findJobsByTitleContainingIgnoreCase(@Param("title") String title,Pageable pageable);
    Page<Job> findAll(Pageable pageable);
    Job findJobByIdAndAppUser(Long id, AppUser user);
}
