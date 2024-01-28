package org.example.youth_be.comment.repository;

import org.example.youth_be.comment.domain.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
