package org.example.youth_be.artwork.repository;

import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtworkRepository extends JpaRepository<ArtworkEntity, Long> {
}
