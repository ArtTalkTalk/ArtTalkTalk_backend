package org.example.youth_be.like.repository;

import org.example.youth_be.like.domain.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
}
