package com.projects.symbolRecognition.exception;

public record ExceptionMessageDTO (
    String time,
    String exception,
    String message,
    ErrorCodes errorCode,
    Boolean showPopUpInUI,
    String applicationName) {}