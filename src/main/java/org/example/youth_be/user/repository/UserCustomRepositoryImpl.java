package org.example.youth_be.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.youth_be.user.domain.QUserEntity;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.enums.SocialTypeEnum;

import java.util.Optional;

@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;
    private QUserEntity userEntity = QUserEntity.userEntity;

    @Override
    public Optional<UserEntity> findBySocialIdAndSocialType(String socialId, SocialTypeEnum socialType) {
        return Optional.ofNullable(jpaQueryFactory.select(userEntity)
                .from(userEntity)
                .where(
                        userEntity.socialId.eq(socialId),
                        userEntity.socialType.eq(socialType)
                ).fetchOne());
    }
}
