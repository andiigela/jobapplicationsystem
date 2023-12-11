package com.ubt.andi.jobapp.repositories;
import com.ubt.andi.jobapp.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job,Long> {
    List<Job> findAllByAppUser_Id(@Param("id") Long id);
    List<Job> findJobsByTitleContainingIgnoreCase(@Param("title") String title);
}
