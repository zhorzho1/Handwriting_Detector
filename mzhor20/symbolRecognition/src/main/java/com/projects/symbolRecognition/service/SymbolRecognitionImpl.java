package com.projects.symbolRecognition.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.symbolRecognition.exception.impl.ExternalApiException;
import com.projects.symbolRecognition.exception.impl.FileProcessingException;
import com.projects.symbolRecognition.exception.impl.JsonParsingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SymbolRecognitionImpl implements SymbolRecognition {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${symbol.recognition.url}")
    private String symbolRecognitionUrl;

    @Value("${symbol.recognition.application.name}")
    private String symbolRecognitionAppName;

    @Value("${application.name}")
    private String applicationName;


    @Override
    public Character processImageAndGetSymbol(MultipartFile image) {
        log.info("Processing image for symbol recognition.");
        if (image.isEmpty()) {
            log.warn("Uploaded file is empty. Throwing FileProcessingException.");
            throw new FileProcessingException("Uploaded file is empty.");
        }

        ResponseEntity<String> response = sendImageToRecognitionService(image);
        JsonNode jsonResponse = parseResponse(response);

        return extractCharacter(jsonResponse);
    }

    private ResponseEntity<String> sendImageToRecognitionService(MultipartFile image) {
        log.info("Sending image to external symbol recognition service at URL: {}", symbolRecognitionUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", image.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(symbolRecognitionUrl, HttpMethod.POST, requestEntity, String.class);
            log.info("Received response from external API with status code: {}", response.getStatusCode());
            return response;
        } catch (RestClientException e) {
            log.error("Error sending request to external API: {}", e.getMessage());
            throw new ExternalApiException("Request couldn't be sent to " + symbolRecognitionAppName, applicationName);
        }
    }

    private JsonNode parseResponse(ResponseEntity<String> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("External API responded with a non-successful status: {}", response.getStatusCode());
            throw new ExternalApiException("External API responded with a non-successful status: "
                    + response.getStatusCode(), symbolRecognitionAppName);
        }

        try {
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            log.info("Successfully parsed JSON response from external API.");
            return jsonResponse;
        } catch (IOException e) {
            log.error("Error parsing JSON response: {}", e.getMessage());
            throw new JsonParsingException("Error parsing JSON response from external API", e);
        }
    }

    private Character extractCharacter(JsonNode jsonResponse) {
        String character = jsonResponse.get("character").asText();
        log.info("Extracted character from JSON response: {}", character);
        return character.charAt(0);
    }

}
