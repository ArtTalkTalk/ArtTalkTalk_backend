package org.example.youth_be.user.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.youth_be.user.domain.UserEntity;

@Getter
@AllArgsConstructor
public class UserProfileDto {
    private Long userId;
    private String nickname;
    private String major;
    private String description;
    private Long totalLikeCount;
    private Long followerCount;

    static public UserProfileDto of(UserEntity user) {
        return new UserProfileDto(
                user.getUserId(),
                user.getNickname(),
                user.getMajor(),
                user.getDescription(),
                user.getTotalLikeCount(),
                user.getFollowerCount()
        );
    }
}
