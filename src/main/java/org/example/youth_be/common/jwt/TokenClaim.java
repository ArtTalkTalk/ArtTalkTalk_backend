package org.example.youth_be.common.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.youth_be.user.enums.UserRoleEnum;

import java.util.Objects;

import static org.example.youth_be.common.jwt.TokenProvider.KEY_USER_ID;
import static org.example.youth_be.common.jwt.TokenProvider.KEY_USER_ROLE;

@Getter
@AllArgsConstructor
public class TokenClaim {
    @JsonProperty("user_id")
    private Long userId;
    private String sub;
    private Long iat;
    private Long exp;
    @JsonProperty("user_role")
    private UserRoleEnum userRole;

    static TokenClaim of(Claims claims) {
        return new TokenClaim(claims.get(KEY_USER_ID, Long.class),
                claims.getSubject(),
                claims.getIssuedAt().toInstant().getEpochSecond(),
                claims.getExpiration().getTime(),
                UserRoleEnum.fromValue((String) claims.get(KEY_USER_ROLE)));
    }

    public boolean isNotAuthorized(Long userId) {
        return !Objects.equals(this.userId, userId);
    }
}
