package org.example.youth_be.image.repository;

import org.example.youth_be.image.domain.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    List<ImageEntity> findByArtworkId(Long artworkId);
}
