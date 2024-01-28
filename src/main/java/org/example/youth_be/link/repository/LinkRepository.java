package org.example.youth_be.link.repository;

import org.example.youth_be.link.domain.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<LinkEntity, Long> {
}
