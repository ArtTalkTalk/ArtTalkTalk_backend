package org.example.youth_be.user.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.youth_be.user.enums.SocialTypeEnum;

@Getter
@AllArgsConstructor
public class LoginRequest {
    @NotNull
    private String socialId;
    @NotNull
    private SocialTypeEnum socialType;
}
