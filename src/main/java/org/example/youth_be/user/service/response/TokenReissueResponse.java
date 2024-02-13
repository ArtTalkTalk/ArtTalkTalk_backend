package org.example.youth_be.user.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenReissueResponse {
    private String accessToken;
    private String refreshToken;
}
