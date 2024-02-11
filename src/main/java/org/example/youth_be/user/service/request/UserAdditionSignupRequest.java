package org.example.youth_be.user.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAdditionSignupRequest {
    @Schema(description = "프로필 이미지 URL")
    private String profileImageUrl;
    @Schema(description = "닉네임")
    @NotNull(message = "닉네임은 반드시 있어야 합니다")
    private String nickname;
    @Schema(description = "활동 분야")
    private String activityField;
    @Schema(description = "활동 지역")
    private String activityArea;
    @Schema(description = "소개글")
    private String description;
}
