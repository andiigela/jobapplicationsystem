package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview,Long> {
}
