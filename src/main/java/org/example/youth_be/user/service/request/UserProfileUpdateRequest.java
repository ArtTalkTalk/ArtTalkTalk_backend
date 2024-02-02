package org.example.youth_be.user.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileUpdateRequest {
    @Schema(description = "프로필 이미지 url", example = "https://s3.abc.com")
    private String profileImageUrl;
    @Schema(description = "닉네임 (중복 체크)", example = "홍길동")
    private String nickname;
    @Schema(description = "활동 분야", example = "3D Art")
    private String activityField;
    @Schema(description = "활동 지역", example = "이태원")
    private String activityArea;
    @Schema(description = "소개글")
    private String description;
}
