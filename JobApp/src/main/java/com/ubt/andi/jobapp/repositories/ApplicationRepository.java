package com.ubt.andi.jobapp.repositories;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Application;
import com.ubt.andi.jobapp.models.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    Application findByAppUserAndJob(AppUser user, Job job);
    Page<Application> findAllByJobOrderByCreationDateDesc(Job job,Pageable pageable);
}
