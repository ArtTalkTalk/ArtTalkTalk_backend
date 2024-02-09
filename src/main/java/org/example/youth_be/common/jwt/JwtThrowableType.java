package org.example.youth_be.common.jwt;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JwtThrowableType {
    EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    SIGNATURE_INVALID(HttpStatus.BAD_REQUEST, "시그니처와 일치하지 않는 JWT 토큰입니다."),
    UNSUPPORTED(HttpStatus.BAD_REQUEST, "지원하지 않는 JWT 토큰입니다."),
    MALFORMED(HttpStatus.BAD_REQUEST, "올바르지 않은 토큰입니다."),
    NULL_OR_EMPTY(HttpStatus.BAD_REQUEST, "JWT 토큰이 없습니다."),
    UNHANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "처리할 수 없는 예외가 발생했습니다.");

    private final HttpStatus statusCode;
    private final String description;

    JwtThrowableType(HttpStatus statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }
}
