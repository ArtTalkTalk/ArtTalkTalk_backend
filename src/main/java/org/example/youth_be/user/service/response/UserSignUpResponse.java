package org.example.youth_be.user.service.response;

import lombok.Builder;
import lombok.Getter;
import org.example.youth_be.user.enums.UserRoleEnum;

@Getter
@Builder
public class UserSignUpResponse {
    private Long userId;
    private UserRoleEnum role;
    private String accessToken;
}
