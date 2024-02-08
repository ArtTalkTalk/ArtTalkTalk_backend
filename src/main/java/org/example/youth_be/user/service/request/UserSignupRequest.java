package org.example.youth_be.user.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.example.youth_be.user.enums.SocialTypeEnum;
import org.example.youth_be.user.enums.UserRoleEnum;

@Getter
public class UserSignupRequest {
    @Schema(description = "프로필 이미지 URL")
    private String profileImageUrl;
    @Schema(description = "소셜 타입", example = "KAKAO")
    private SocialTypeEnum socialType;
    @Schema(description = "유저 권한", example = "REGULAR")
    private UserRoleEnum userRole;
    @Schema(description = "소셜 ID")
    @NotEmpty(message = "소셜 ID는 반드시 있어야 합니다")
    private String socialId;
    @Schema(description = "닉네임")
    private String nickname;
    @Schema(description = "활동 분야")
    private String activityField;
    @Schema(description = "활동 지역")
    private String activityArea;
    @Schema(description = "소개글")
    private String description;
}
