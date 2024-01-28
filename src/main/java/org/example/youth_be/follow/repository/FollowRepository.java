package org.example.youth_be.follow.repository;

import org.example.youth_be.follow.domain.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
}
