package com.projects.symbolRecognition.exception;

import com.projects.symbolRecognition.config.ApplicationInfo;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ErrorCodes errorCode;
    private final Boolean showPopUpInUI;
    private final HttpStatus status;
    private final String applicationName;


    public BaseException(String message, ErrorCodes errorCode, Boolean showPopUpInUI,
                         HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.showPopUpInUI = showPopUpInUI;
        this.status = status;
        this.applicationName = ApplicationInfo.getApplicationName();
    }

    public BaseException(String message, ErrorCodes errorCode, Boolean showPopUpInUI,
                         HttpStatus status, String applicationName) {
        super(message);
        this.errorCode = errorCode;
        this.showPopUpInUI = showPopUpInUI;
        this.applicationName = applicationName;
        this.status = status;
    }

    public BaseException(String message, ErrorCodes errorCode, Boolean showPopUpInUI,
                         HttpStatus status, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.showPopUpInUI = showPopUpInUI;
        this.status = status;
        this.applicationName = ApplicationInfo.getApplicationName();
    }

    public BaseException(String message, ErrorCodes errorCode, Boolean showPopUpInUI,
                         HttpStatus status, String applicationName, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.showPopUpInUI = showPopUpInUI;
        this.applicationName = applicationName;
        this.status = status;
    }
}