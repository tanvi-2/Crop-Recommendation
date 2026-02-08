package com.crop.cropbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for Crop Prediction Response
 * 
 * This class represents the predicted crop recommendation
 * returned by the ML model.
 */
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates no-args constructor
@AllArgsConstructor // Generates all-args constructor
@Builder // Enables builder pattern for object creation
public class CropResponse {

    /**
     * The recommended crop name based on the input conditions
     * Examples: "Rice", "Wheat", "Maize", "Cotton", etc.
     */
    private String crop;

    /**
     * Optional message for additional information or error details
     */
    private String message;

    /**
     * Factory method to create a successful response
     * 
     * @param crop The predicted crop name
     * @return CropResponse with crop name and success message
     */
    public static CropResponse success(String crop) {
        return CropResponse.builder()
                .crop(crop)
                .message("Prediction successful")
                .build();
    }

    /**
     * Factory method to create an error response
     * 
     * @param errorMessage The error message to include
     * @return CropResponse with null crop and error message
     */
    public static CropResponse error(String errorMessage) {
        return CropResponse.builder()
                .crop(null)
                .message(errorMessage)
                .build();
    }
}
