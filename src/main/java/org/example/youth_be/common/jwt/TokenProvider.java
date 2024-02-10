package org.example.youth_be.common.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.enums.UserRoleEnum;

public interface TokenProvider {
    String SUBJECT_ACCESS_TOKEN = "access";
    String SUBJECT_REFRESH_TOKEN = "refresh";
    String KEY_USER_ID = "user_id";
    String KEY_USER_ROLE = "user_role";
    TokenClaim getClaim(String token);
    String generateToken(Long userId, UserRoleEnum userRole);
    UserEntity getUserFromToken(String token);
    default ParsedTokenInfo parseToken(String token) {
        try {
            TokenClaim tokenClaim = getClaim(token);
            return new ParsedTokenInfo(tokenClaim, null);
        } catch (UnsupportedJwtException ex) {
            return new ParsedTokenInfo(null, JwtThrowableType.UNSUPPORTED);
        } catch (MalformedJwtException ex) {
            return new ParsedTokenInfo(null, JwtThrowableType.MALFORMED);
        } catch (ExpiredJwtException ex) {
            return new ParsedTokenInfo(TokenClaim.of(ex.getClaims()), JwtThrowableType.EXPIRED);
        } catch (IllegalArgumentException ex) {
            return new ParsedTokenInfo(null, JwtThrowableType.NULL_OR_EMPTY);
        } catch (Exception ex) {
            return new ParsedTokenInfo(null, JwtThrowableType.UNHANDLED_EXCEPTION);
        }
    }
}
