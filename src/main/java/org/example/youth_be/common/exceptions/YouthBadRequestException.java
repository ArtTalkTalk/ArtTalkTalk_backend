package org.example.youth_be.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class YouthBadRequestException extends YouthException {
    public YouthBadRequestException(String clientMessage, String debugMessage) {
        super(HttpStatus.BAD_REQUEST, clientMessage, debugMessage);
    }
}
