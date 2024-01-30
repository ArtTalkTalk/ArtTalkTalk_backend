package org.example.youth_be.artwork.repository;

import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkRepository extends JpaRepository<ArtworkEntity, Long>, ArtworkRepositoryCustom {

}
