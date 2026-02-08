package com.crop.cropbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Smart Crop Recommendation & Yield Prediction System
 * 
 * Main Spring Boot Application Entry Point
 * 
 * This application provides a REST API for crop recommendation
 * based on soil and weather parameters. It integrates with an
 * external Flask ML API for making predictions.
 * 
 * Key Features:
 * - REST API endpoint for crop prediction
 * - Integration with external ML service
 * - Input validation
 * - Proper error handling
 * 
 * Architecture:
 * - Controller Layer: Handles HTTP requests/responses
 * - Service Layer: Contains business logic
 * - DTO Layer: Data Transfer Objects
 * - Config Layer: Application configuration beans
 * 
 * Base API URL: http://localhost:8080/api/crop
 * 
 * @author Crop Prediction System
 * @version 1.0.0
 */
@SpringBootApplication
public class CropbackendApplication {

	/**
	 * Main method - Application entry point
	 * 
	 * Starts the Spring Boot application with embedded Tomcat server.
	 * Server runs on port 8080 by default.
	 * 
	 * @param args Command line arguments (optional)
	 */
	public static void main(String[] args) {
		SpringApplication.run(CropbackendApplication.class, args);

		// Print startup message
		System.out.println("\n========================================");
		System.out.println("  🌾 CROP PREDICTION SYSTEM STARTED 🌾  ");
		System.out.println("========================================");
		System.out.println("  API Base URL: http://localhost:8080");
		System.out.println("  Predict Endpoint: POST /api/crop/predict");
		System.out.println("  Health Check: GET /api/crop/health");
		System.out.println("========================================\n");
	}

}
