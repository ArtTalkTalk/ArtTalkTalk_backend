package org.example.youth_be.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class YouthInternalException extends YouthException{
    public YouthInternalException(String clientMessage, String debugMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, clientMessage, debugMessage);
    }
}
