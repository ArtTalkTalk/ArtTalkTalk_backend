package org.example.youth_be.comment.repository;

import org.example.youth_be.comment.domain.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long>, CommentCustomRepository {

    List<CommentEntity> findByArtworkId(Long artworkId);
}
