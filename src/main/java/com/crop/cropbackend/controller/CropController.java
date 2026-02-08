package com.crop.cropbackend.controller;

import com.crop.cropbackend.dto.CropRequest;
import com.crop.cropbackend.dto.CropResponse;
import com.crop.cropbackend.service.CropService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for Crop Prediction Endpoints
 * 
 * This controller exposes REST APIs for crop recommendation
 * based on soil and weather conditions.
 * 
 * Base URL: /api/crop
 */
@RestController
@RequestMapping("/api/crop")
@Slf4j // Enables logging
@RequiredArgsConstructor // Constructor injection for dependencies
@CrossOrigin(origins = "*") // Allow CORS from all origins (configure for production)
public class CropController {

    // CropService for handling business logic (injected via constructor)
    private final CropService cropService;

    /**
     * Endpoint to predict the best crop based on input parameters
     * 
     * URL: POST /api/crop/predict
     * 
     * Request Body Example:
     * {
     * "nitrogen": 90,
     * "phosphorus": 42,
     * "potassium": 43,
     * "temperature": 20.87,
     * "humidity": 82.0,
     * "rainfall": 202.93
     * }
     * 
     * Response Example (Success):
     * {
     * "crop": "Rice",
     * "message": "Prediction successful"
     * }
     * 
     * @param request CropRequest object containing soil and weather parameters
     * @return ResponseEntity with CropResponse or error details
     */
    @PostMapping("/predict")
    public ResponseEntity<?> predictCrop(@Valid @RequestBody CropRequest request) {
        log.info("Received crop prediction request: {}", request);

        try {
            // Call service layer to get prediction
            CropResponse response = cropService.predictCrop(request);

            // Check if prediction was successful
            if (response.getCrop() != null) {
                log.info("Returning successful prediction: {}", response.getCrop());
                return ResponseEntity.ok(response);
            } else {
                // ML service returned an error
                log.warn("Prediction failed: {}", response.getMessage());
                return ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(response);
            }

        } catch (Exception e) {
            // Handle any unexpected errors
            log.error("Error processing crop prediction: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CropResponse.error("An unexpected error occurred"));
        }
    }

    /**
     * Exception handler for validation errors
     * 
     * This method is triggered when @Valid validation fails.
     * It collects all validation errors and returns them in a structured format.
     * 
     * @param ex The MethodArgumentNotValidException containing validation errors
     * @return ResponseEntity with validation error details
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Validation failed");

        // Collect all field-level validation errors
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                        (existing, replacement) -> existing // Handle duplicate keys
                ));

        errorResponse.put("fieldErrors", fieldErrors);

        log.warn("Validation errors: {}", fieldErrors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Health check endpoint
     * 
     * URL: GET /api/crop/health
     * 
     * @return Simple health status response
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Crop Prediction Service is running");
        return ResponseEntity.ok(response);
    }
}
