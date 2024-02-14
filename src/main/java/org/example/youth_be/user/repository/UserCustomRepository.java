package org.example.youth_be.user.repository;

import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.enums.SocialTypeEnum;

import java.util.Optional;

interface UserCustomRepository {
    Optional<UserEntity> findBySocialIdAndSocialType(String socialId, SocialTypeEnum socialType);
}
