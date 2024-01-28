package org.example.youth_be.common.exceptions;


import lombok.Getter;
import org.example.youth_be.common.enums.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
abstract public class YouthException extends RuntimeException {
    protected LogLevel logLevel = LogLevel.INFO;
    protected HttpStatus statusCode = HttpStatus.BAD_REQUEST;
    protected String clientMessage = null;
    protected String debugMessage = null;

    public YouthException(HttpStatus statusCode, String clientMessage, String debugMessage) {
        this.statusCode = statusCode;
        this.clientMessage = clientMessage;
        this.debugMessage = debugMessage;
    }
}
