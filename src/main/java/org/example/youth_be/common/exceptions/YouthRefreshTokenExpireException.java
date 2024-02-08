package org.example.youth_be.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class YouthRefreshTokenExpireException extends YouthException{
    public YouthRefreshTokenExpireException(String clientMessage, String debugMessage) {
        super(HttpStatus.FORBIDDEN, clientMessage, debugMessage);
    }
}
