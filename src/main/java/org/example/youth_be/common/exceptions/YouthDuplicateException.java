package org.example.youth_be.common.exceptions;

import org.springframework.http.HttpStatus;

public class YouthDuplicateException extends YouthException{
    public YouthDuplicateException(String clientMessage, String debugMessage) {
        super(HttpStatus.CONFLICT, clientMessage, debugMessage);
    }
}
