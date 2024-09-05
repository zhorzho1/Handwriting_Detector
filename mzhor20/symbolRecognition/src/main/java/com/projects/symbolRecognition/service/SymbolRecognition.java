package com.projects.symbolRecognition.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface SymbolRecognition {

    Character processImageAndGetSymbol(@NotNull MultipartFile image);
}
