package org.example.youth_be.user.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.youth_be.user.enums.UserRoleEnum;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private UserRoleEnum userRole;
}
