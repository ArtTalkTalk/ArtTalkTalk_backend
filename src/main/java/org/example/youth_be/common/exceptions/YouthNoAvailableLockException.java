package org.example.youth_be.common.exceptions;

import org.springframework.http.HttpStatus;

public class YouthNoAvailableLockException extends YouthException{
    public YouthNoAvailableLockException(String clientMessage, String debugMessage) {
        super(HttpStatus.BAD_REQUEST, clientMessage, debugMessage);
    }
}
