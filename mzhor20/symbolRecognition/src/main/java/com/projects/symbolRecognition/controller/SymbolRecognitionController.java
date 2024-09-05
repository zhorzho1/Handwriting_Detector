package com.projects.symbolRecognition.controller;

import com.projects.symbolRecognition.exception.impl.FileTypeException;
import com.projects.symbolRecognition.service.SymbolRecognition;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/symbolRecognition")
@Validated
@RequiredArgsConstructor

public class SymbolRecognitionController {

    private final SymbolRecognition symbolRecognition;

    @Operation(summary = "Upload an image and retrieve a symbol",
            description = "This endpoint accepts an image file (JPEG or PNG) and processes it to extract a symbol.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Character> getSymbolFromImage(@RequestPart("image") @NotNull MultipartFile image) {
        log.info("Received a request to process an image.");

        String contentType = image.getContentType();
        log.debug("Image content type: {}", contentType);

        if (!MediaType.IMAGE_JPEG_VALUE.equals(contentType) && !MediaType.IMAGE_PNG_VALUE.equals(contentType)) {
            log.warn("Invalid file type received: {}. Only JPEG or PNG are allowed.", contentType);
            throw new FileTypeException("Invalid file type. Only JPEG or PNG are allowed.");
        }

        Character predictedSymbol = symbolRecognition.processImageAndGetSymbol(image);
        log.info("Successfully processed image. Predicted symbol: {}", predictedSymbol);

        return ResponseEntity.ok(predictedSymbol);
    }
}