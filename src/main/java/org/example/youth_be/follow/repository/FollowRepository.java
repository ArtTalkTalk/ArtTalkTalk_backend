package org.example.youth_be.follow.repository;

import org.example.youth_be.follow.domain.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    Optional<FollowEntity> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
