package com.projects.symbolRecognition.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    private ResponseEntity<ExceptionMessageDTO> handleBaseException(BaseException e) {
        ExceptionMessageDTO dto = convertAndLogException(e, e.getErrorCode() + " - " + e.getErrorCode().getMessage());
        return new ResponseEntity<>(dto, e.getStatus());

    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ExceptionMessageDTO> handleGeneralException(Exception e) {
        BaseException baseException = new BaseException(e.getMessage(), ErrorCodes.INTERNAL_xSERVER_ERROR, false,
                HttpStatus.INTERNAL_SERVER_ERROR) {};
        ExceptionMessageDTO dto = convertAndLogException(baseException, "General Exception");
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ExceptionMessageDTO convertAndLogException(BaseException e, String logMessage) {
        ExceptionMessageDTO message = new ExceptionMessageDTO(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                e.getClass().getSimpleName(),
                e.getMessage(),
                e.getErrorCode(),
                e.getShowPopUpInUI(),
                e.getApplicationName());

        log.error("{} Occurred: {}. Stack Trace: ", logMessage, message, e);
        return message;
    }
}
