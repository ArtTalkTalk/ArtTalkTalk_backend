package org.example.youth_be.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.youth_be.common.entity.BaseEntity;
import org.example.youth_be.user.enums.SocialTypeEnum;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialTypeEnum socialType;

    @Column(length = 1000)
    private String profileImageUrl;

    private String socialId;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 50)
    private String activityField;

    @Column(length = 255)
    private String activityArea;

    private String description;

    private Long totalLikeCount;

    private Long followerCount;

    @Builder
    public UserEntity(Long userId, SocialTypeEnum socialType, String profileImageUrl, String socialId, String nickname, String activityField, String activityArea, String description, Long totalLikeCount, Long followerCount) {
        this.userId = userId;
        this.socialType = socialType;
        this.profileImageUrl = profileImageUrl;
        this.socialId = socialId;
        this.nickname = nickname;
        this.activityField = activityField;
        this.activityArea = activityArea;
        this.description = description;
        this.totalLikeCount = totalLikeCount;
        this.followerCount = followerCount;
    }

    public void updateProfile(String profileImageUrl, String nickname, String activityField, String activityArea, String description) {
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.activityField = activityField;
        this.activityArea = activityArea;
        this.description = description;
    }
}
