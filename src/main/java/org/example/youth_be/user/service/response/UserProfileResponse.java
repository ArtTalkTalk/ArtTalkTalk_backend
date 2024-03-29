package org.example.youth_be.user.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.domain.UserLinkEntity;
import org.example.youth_be.user.enums.UserRoleEnum;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UserProfileResponse {
    @Schema(description = "유저 ID")
    private Long userId;
    @Schema(description = "유저 권한", example = "ASSOCIATE, REGULAR")
    private UserRoleEnum userRole;
    @Schema(description = "닉네임")
    private String nickname;
    @Schema(description = "활동 분야")
    private String activityField;
    @Schema(description = "활동 지역")
    private String activityArea;
    @Schema(description = "한 줄 소개", nullable = true)
    private String description;
    @Schema(description = "프로필 이미지 URL", nullable = true)
    private String profileImageUrl;
    @Schema(description = "좋아요 수")
    private Long totalLikeCount;
    @Schema(description = "팔로워 수")
    private Long followerCount;
    @Schema(description = "유저의 외부 링크 리스트")
    private List<LinkResponse> links;
    @Schema(description = "유저의 팔로우 ID\n\nnote: 유저가 팔로우를 누르지 않았다면 null입니다")
    private Long followId;

    static public UserProfileResponse of(UserEntity user, List<UserLinkEntity> userLinkEntities, Long followId) {
        return new UserProfileResponse(
                user.getUserId(),
                user.getUserRole(),
                user.getNickname(),
                user.getActivityField(),
                user.getActivityArea(),
                user.getDescription(),
                user.getProfileImageUrl(),
                user.getTotalLikeCount(),
                user.getFollowerCount(),
                userLinkEntities.stream().map(LinkResponse::of).collect(Collectors.toList()),
                followId
        );
    }
}
