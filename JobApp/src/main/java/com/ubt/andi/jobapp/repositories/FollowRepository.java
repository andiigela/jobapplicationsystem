package com.ubt.andi.jobapp.repositories;

import com.ubt.andi.jobapp.models.Follow;
import com.ubt.andi.jobapp.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    Follow findFollowByFollowingAndFollower(Profile following, Profile follower);
}
