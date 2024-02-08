package org.example.youth_be.user.repository;

import org.example.youth_be.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    boolean existsByNickname(String nickname);
}
