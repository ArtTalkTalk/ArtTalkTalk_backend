package org.example.youth_be.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class YouthNotFoundException extends YouthException {
    public YouthNotFoundException(String clientMessage, String debugMessage) {
        super(HttpStatus.NOT_FOUND, clientMessage, debugMessage);
    }
}
