package com.ubt.andi.jobapp.repositories;
import com.ubt.andi.jobapp.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job,Long> {
}
