package org.example.youth_be.user.service.request;

import lombok.Getter;
import org.example.youth_be.user.enums.SocialTypeEnum;
import org.example.youth_be.user.enums.UserRoleEnum;

import java.util.UUID;
import java.util.random.RandomGenerator;

@Getter
public class DevUserProfileCreateRequest {
    private String profileImageUrl = null;
    private SocialTypeEnum socialType = SocialTypeEnum.GOOGLE;
    private UserRoleEnum userRole = UserRoleEnum.REGULAR;
    private String socialId = "social";
    private String nickname = UUID.randomUUID().toString();
    private String major = "아무런학과";
    private String description = "아무런설명";
    private String link = "아무런링크";
    private Long totalLikeCount = Math.abs(RandomGenerator.getDefault().nextLong() % 100);
    private Long followerCount = Math.abs(RandomGenerator.getDefault().nextLong() % 100);
}
