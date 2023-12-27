package com.ubt.andi.jobapp.repositories;
import com.ubt.andi.jobapp.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
}
