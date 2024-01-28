package org.example.youth_be.user.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.youth_be.user.domain.UserEntity;

@Getter
@AllArgsConstructor
public class UserProfileDto {
    @Schema(description = "유저 ID")
    private Long userId;
    @Schema(description = "닉네임")
    private String nickname;
    @Schema(description = "학과")
    private String major;
    @Schema(description = "한 줄 소개", nullable = true)
    private String description;
    @Schema(description = "좋아요 수")
    private Long totalLikeCount;
    @Schema(description = "팔로워 수")
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
