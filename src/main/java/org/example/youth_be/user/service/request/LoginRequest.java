package org.example.youth_be.user.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.youth_be.user.enums.SocialTypeEnum;

@Getter
@AllArgsConstructor
public class LoginRequest {
    private String socialId;
    private SocialTypeEnum socialType;
}
