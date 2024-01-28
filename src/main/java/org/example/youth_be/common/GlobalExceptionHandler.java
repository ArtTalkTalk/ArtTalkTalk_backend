package org.example.youth_be.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.common.exceptions.YouthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = {RestController.class})
@Slf4j
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({YouthException.class})
    private ResponseEntity<ErrorResponse> youthException(YouthException exception) {
        if (exception.getDebugMessage() == null) {
            exception.getLogLevel().log(logger, exception.getClientMessage());
        } else {
            exception.getLogLevel().log(logger, exception.getDebugMessage());
        }
        return ResponseEntity.status(exception.getStatusCode()).body(new ErrorResponse(exception.getClientMessage()));
    }
}

@Getter
@AllArgsConstructor
class ErrorResponse {
    private String message;
}
