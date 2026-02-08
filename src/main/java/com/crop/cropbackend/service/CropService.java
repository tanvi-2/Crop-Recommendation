package com.crop.cropbackend.service;

import com.crop.cropbackend.dto.CropRequest;
import com.crop.cropbackend.dto.CropResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Service layer for Crop Prediction
 * 
 * This service handles the business logic for crop prediction.
 * It communicates with an external Flask ML API to get predictions
 * based on soil and weather parameters.
 */
@Service
@Slf4j // Enables logging with Lombok (creates 'log' variable)
@RequiredArgsConstructor // Generates constructor for final fields (dependency injection)
public class CropService {

    // RestTemplate for making HTTP requests (injected via constructor)
    private final RestTemplate restTemplate;

    // Flask ML API URL - loaded from application.properties
    @Value("${flask.api.url}")
    private String flaskApiUrl;

    /**
     * Predicts the best crop based on input parameters
     * 
     * This method:
     * 1. Prepares the request for the external ML API
     * 2. Sends the request to Flask API
     * 3. Parses and returns the response
     * 
     * @param request CropRequest containing soil and weather parameters
     * @return CropResponse with the predicted crop or error message
     */
    public CropResponse predictCrop(CropRequest request) {
        log.info("Processing crop prediction request: {}", request);

        try {
            // Step 1: Prepare HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Step 2: Create HTTP entity with request body and headers
            HttpEntity<CropRequest> httpEntity = new HttpEntity<>(request, headers);

            log.debug("Calling Flask API at: {}", flaskApiUrl);

            // Step 3: Make POST request to Flask ML API
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(
                    flaskApiUrl, // URL of Flask API
                    httpEntity, // Request body and headers
                    (Class<Map<String, Object>>) (Class<?>) Map.class // Response type
            );

            log.info("Flask API response status: {}", response.getStatusCode());

            // Step 4: Extract crop from response
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("crop")) {
                String predictedCrop = responseBody.get("crop").toString();
                log.info("Predicted crop: {}", predictedCrop);
                return CropResponse.success(predictedCrop);
            } else {
                log.warn("Invalid response from Flask API: {}", responseBody);
                return CropResponse.error("Invalid response from ML model");
            }

        } catch (ResourceAccessException e) {
            // Handle connection errors (Flask API not reachable)
            log.error("Failed to connect to Flask API: {}", e.getMessage());
            return CropResponse.error("ML Service is currently unavailable. Please try again later.");

        } catch (HttpClientErrorException e) {
            // Handle 4xx client errors from Flask API
            log.error("Client error from Flask API: {} - {}", e.getStatusCode(), e.getMessage());
            return CropResponse.error("Invalid request to ML Service: " + e.getStatusCode());

        } catch (HttpServerErrorException e) {
            // Handle 5xx server errors from Flask API
            log.error("Server error from Flask API: {} - {}", e.getStatusCode(), e.getMessage());
            return CropResponse.error("ML Service encountered an error. Please try again later.");

        } catch (Exception e) {
            // Handle any other unexpected errors
            log.error("Unexpected error during crop prediction: {}", e.getMessage(), e);
            return CropResponse.error("An unexpected error occurred: " + e.getMessage());
        }
    }
}
