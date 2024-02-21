package org.example.youth_be.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.common.exceptions.YouthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({YouthException.class})
    public ResponseEntity<ErrorResponse> youthException(YouthException exception) {
        if (exception.getDebugMessage() == null) {
            exception.getLogLevel().log(logger, exception.getClientMessage());
        } else {
            exception.getLogLevel().log(logger, exception.getDebugMessage());
        }
        return ResponseEntity.status(exception.getStatusCode()).body(new ErrorResponse(exception.getClientMessage()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleExceptionHandler(HttpServletRequest request, Exception e) {
        log.error("defaultExceptionHandler", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleBadRequest(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("url {}, message: {}",
                request.getRequestURI(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
            MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleBadRequest(
            MissingServletRequestParameterException e, HttpServletRequest request) {
        log.error("url {}, message: {}",
                request.getRequestURI(), e.getParameterName() + " 값이 등록되지 않았습니다.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getParameterName() + " 값이 등록되지 않았습니다."));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Object> handleBadRequest(
            MissingServletRequestPartException e, HttpServletRequest request) {
        log.error("url {}, message: {}",
                request.getRequestURI(), e.getRequestPartName() + " 값을 요청받지 못했습니다.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getRequestPartName() + " 값을 요청받지 못했습니다."));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMultipartExceptionPayloadTooLarge(
            MaxUploadSizeExceededException e, HttpServletRequest request) {
        log.error("url {}, message: {}",
                request.getRequestURI(), "파일 크기가 너무 큽니다.");

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new ErrorResponse("파일 크기가 너무 큽니다."));
    }
}

@Getter
@AllArgsConstructor
class ErrorResponse {
    private String message;
}
