package com.projects.symbolRecognition.exception.impl;

import com.projects.symbolRecognition.exception.BaseException;
import com.projects.symbolRecognition.exception.ErrorCodes;
import org.springframework.http.HttpStatus;

public class ExternalApiException extends BaseException {
    public ExternalApiException(String message, String applicationName) {
        super(message, ErrorCodes.API_CALL_ERROR, false, HttpStatus.SERVICE_UNAVAILABLE, applicationName);
    }

    public ExternalApiException(String message, String applicationName, Throwable cause) {
        super(message, ErrorCodes.API_CALL_ERROR, false, HttpStatus.SERVICE_UNAVAILABLE, applicationName, cause);
    }
}