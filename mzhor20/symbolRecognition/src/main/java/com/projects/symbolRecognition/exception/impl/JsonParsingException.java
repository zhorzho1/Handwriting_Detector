package com.projects.symbolRecognition.exception.impl;

import com.projects.symbolRecognition.exception.BaseException;
import com.projects.symbolRecognition.exception.ErrorCodes;
import org.springframework.http.HttpStatus;

public class JsonParsingException extends BaseException {
    public JsonParsingException(String message) {
        super(message, ErrorCodes.JSON_PARSING_ERROR, false, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public JsonParsingException(String message, Throwable cause) {
        super(message, ErrorCodes.JSON_PARSING_ERROR, false, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }
}