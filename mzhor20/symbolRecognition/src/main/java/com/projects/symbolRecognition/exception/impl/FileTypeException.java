package com.projects.symbolRecognition.exception.impl;

import com.projects.symbolRecognition.exception.BaseException;
import com.projects.symbolRecognition.exception.ErrorCodes;
import org.springframework.http.HttpStatus;

public class FileTypeException extends BaseException {
    public FileTypeException(String message) {
        super(message, ErrorCodes.INVALID_FILE_TYPE, true, HttpStatus.BAD_REQUEST);
    }
}