package org.example.youth_be.user.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.youth_be.user.enums.UserRoleEnum;

@Getter
@AllArgsConstructor
public class DevTokenGenerateRequest {
    @Schema(description = "유저 ID")
    private Long userId;
    @Schema(description = "유저 역할")
    private UserRoleEnum userRole;
    @Schema(description = "액세스 토큰 만료 시간 (초 단위)")
    private Long accessValidityInSeconds;
    @Schema(description = "리프레시 토큰 만료 시간 (초 단위)")
    private Long refreshValidityInSeconds;
}
