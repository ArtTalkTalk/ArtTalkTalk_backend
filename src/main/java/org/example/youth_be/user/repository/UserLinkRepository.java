package org.example.youth_be.user.repository;

import org.example.youth_be.user.domain.UserLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLinkRepository extends JpaRepository<UserLinkEntity, Long> {
    List<UserLinkEntity> findAllByUserId(Long userId);
}
