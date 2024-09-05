package com.projects.symbolRecognition.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodes {
    INTERNAL_xSERVER_ERROR(1L, "Internal Server Error"),
    JSON_PARSING_ERROR(2L, "Error parsing JSON"),
    INVALID_FILE_TYPE(3L, "Invalid file type"),
    FILE_PROCESSING_ERROR(4L, "Error processing file"),
    API_CALL_ERROR(5L, "External API call error");

    private final Long code;
    private final String message;
}
