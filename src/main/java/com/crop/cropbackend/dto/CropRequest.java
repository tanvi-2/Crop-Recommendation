package com.crop.cropbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for Crop Prediction Request
 * 
 * This class represents the input data required for predicting
 * the best crop based on soil and weather conditions.
 * 
 * All fields are mandatory and must be positive numbers.
 */
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates no-args constructor
@AllArgsConstructor // Generates all-args constructor
@Builder // Enables builder pattern for object creation
public class CropRequest {

    /**
     * Nitrogen content in the soil (mg/kg)
     * Typical range: 0-140
     */
    @NotNull(message = "Nitrogen content is required")
    @PositiveOrZero(message = "Nitrogen content must be zero or positive")
    private Double nitrogen;

    /**
     * Phosphorus content in the soil (mg/kg)
     * Typical range: 5-145
     */
    @NotNull(message = "Phosphorus content is required")
    @PositiveOrZero(message = "Phosphorus content must be zero or positive")
    private Double phosphorus;

    /**
     * Potassium content in the soil (mg/kg)
     * Typical range: 5-205
     */
    @NotNull(message = "Potassium content is required")
    @PositiveOrZero(message = "Potassium content must be zero or positive")
    private Double potassium;

    /**
     * Temperature in degrees Celsius
     * Typical range: 8-45
     */
    @NotNull(message = "Temperature is required")
    private Double temperature;

    /**
     * Relative humidity percentage
     * Typical range: 14-100
     */
    @NotNull(message = "Humidity is required")
    @PositiveOrZero(message = "Humidity must be zero or positive")
    private Double humidity;

    /**
     * Rainfall in millimeters
     * Typical range: 20-300
     */
    @NotNull(message = "Rainfall is required")
    @PositiveOrZero(message = "Rainfall must be zero or positive")
    private Double rainfall;
}
