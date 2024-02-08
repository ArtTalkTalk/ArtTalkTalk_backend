package org.example.youth_be.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class YouthUnAuthorizationException extends YouthException {
    public YouthUnAuthorizationException(String clientMessage, String debugMessage) {
        super(HttpStatus.UNAUTHORIZED, clientMessage, debugMessage);
    }
}