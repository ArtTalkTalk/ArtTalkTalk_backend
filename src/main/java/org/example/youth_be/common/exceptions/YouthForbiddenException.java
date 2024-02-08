package org.example.youth_be.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class YouthForbiddenException extends YouthException{
    public YouthForbiddenException(String clientMessage, String debugMessage) {
        super(HttpStatus.FORBIDDEN, clientMessage, debugMessage);
    }
}
