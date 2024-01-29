package org.example.youth_be.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.youth_be.user.enums.SocialTypeEnum;
import org.example.youth_be.user.enums.UserRoleEnum;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
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
    private String major;

    private String description;

    private Long totalLikeCount;

    private Long followerCount;

    @Builder
    public UserEntity(Long userId, SocialTypeEnum socialType, String profileImageUrl, String socialId, String nickname, String major, String description, Long totalLikeCount, Long followerCount) {
        this.userId = userId;
        this.socialType = socialType;
        this.profileImageUrl = profileImageUrl;
        this.socialId = socialId;
        this.nickname = nickname;
        this.major = major;
        this.description = description;
        this.totalLikeCount = totalLikeCount;
        this.followerCount = followerCount;
    }

    public void updateProfile(String profileImageUrl, String nickname, String major, String description) {
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.major = major;
        this.description = description;
    }
}
