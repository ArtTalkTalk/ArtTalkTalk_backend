package org.example.youth_be.user.repository;

import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.enums.SocialTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    boolean existsByNickname(String nickname);
    Optional<UserEntity> findBySocialIdAndSocialType(String socialId, SocialTypeEnum socialType);
}
