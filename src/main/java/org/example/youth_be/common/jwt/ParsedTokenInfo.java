package org.example.youth_be.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParsedTokenInfo {
    private TokenClaim tokenClaim;
    private JwtThrowableType throwableType;

    public boolean isExpired() {
        return throwableType == JwtThrowableType.EXPIRED;
    }

    public boolean isSameUserId(ParsedTokenInfo tokenInfo) {
        return tokenInfo.tokenClaim.getUserId().equals(this.tokenClaim.getUserId());
    }

    public boolean isNormalToken() {
        return throwableType == null;
    }
}
