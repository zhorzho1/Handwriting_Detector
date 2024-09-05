package com.projects.symbolRecognition.exception.impl;

import com.projects.symbolRecognition.exception.BaseException;
import com.projects.symbolRecognition.exception.ErrorCodes;
import org.springframework.http.HttpStatus;

public class FileProcessingException extends BaseException {
    public FileProcessingException(String message) {
        super(message, ErrorCodes.FILE_PROCESSING_ERROR, false, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, ErrorCodes.FILE_PROCESSING_ERROR, false, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }
}