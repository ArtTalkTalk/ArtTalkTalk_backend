package org.example.youth_be.like.repository;

import org.example.youth_be.like.domain.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<Long> findByArtworkIdAndUserId(Long artworkId, Long userId);
}
