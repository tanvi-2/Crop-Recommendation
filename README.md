# 🌾 Smart Crop Recommendation & Yield Prediction System

A Java Spring Boot backend application that provides crop recommendations based on soil nutrients and weather conditions using Machine Learning.

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Running](#installation--running)
- [API Documentation](#api-documentation)
- [Sample Requests & Responses](#sample-requests--responses)
- [Configuration](#configuration)
- [Error Handling](#error-handling)

---

## 🔍 Overview

This application provides a REST API for predicting the best crop to plant based on:
- **Soil Nutrients**: Nitrogen (N), Phosphorus (P), Potassium (K)
- **Weather Conditions**: Temperature, Humidity, Rainfall

The system integrates with an external Flask ML API that runs the prediction model.

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming Language |
| Spring Boot | 3.3.5 | Application Framework |
| Maven | 3.x | Build Tool |
| REST APIs | - | API Architecture |
| RestTemplate | - | HTTP Client |
| Lombok | 1.18.x | Boilerplate Reduction |
| Jakarta Validation | - | Input Validation |

---

## 📁 Project Structure

```
cropbackend/
├── pom.xml                          # Maven configuration
├── src/
│   └── main/
│       ├── java/
│       │   └── com/crop/cropbackend/
│       │       ├── CropbackendApplication.java    # Main application entry
│       │       ├── controller/
│       │       │   └── CropController.java        # REST endpoints
│       │       ├── service/
│       │       │   └── CropService.java           # Business logic
│       │       ├── dto/
│       │       │   ├── CropRequest.java           # Request DTO
│       │       │   └── CropResponse.java          # Response DTO
│       │       └── config/
│       │           └── RestTemplateConfig.java    # HTTP client config
│       └── resources/
│           └── application.properties             # App configuration
└── README.md
```

### Layer Architecture

| Layer | Package | Responsibility |
|-------|---------|----------------|
| **Controller** | `controller` | Handle HTTP requests/responses |
| **Service** | `service` | Business logic & external API calls |
| **DTO** | `dto` | Data Transfer Objects |
| **Config** | `config` | Application configuration beans |

---

## ⚙️ Prerequisites

1. **Java 17** or higher
   ```bash
   java -version
   ```

2. **Maven 3.x** or higher
   ```bash
   mvn -version
   ```

3. **Flask ML API** running at `http://localhost:5000/predict`

---

## 🚀 Installation & Running

### Step 1: Clone/Navigate to the project
```bash
cd cropbackend
```

### Step 2: Build the project
```bash
mvn clean install
```

### Step 3: Run the application
```bash
mvn spring-boot:run
```

### Alternative: Run using JAR
```bash
# Build the JAR
mvn clean package

# Run the JAR
java -jar target/cropbackend-1.0.0.jar
```

### Step 4: Verify the application is running

Visit: [http://localhost:8080/api/crop/health](http://localhost:8080/api/crop/health)

Expected response:
```json
{
    "status": "UP",
    "message": "Crop Prediction Service is running"
}
```

---

## 📡 API Documentation

### Base URL
```
http://localhost:8080/api/crop
```

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/predict` | Get crop recommendation |
| `GET` | `/health` | Health check |

---

## 📝 Sample Requests & Responses

### 1. Crop Prediction

**Request:**
```bash
curl -X POST http://localhost:8080/api/crop/predict \
  -H "Content-Type: application/json" \
  -d '{
    "nitrogen": 90,
    "phosphorus": 42,
    "potassium": 43,
    "temperature": 20.87,
    "humidity": 82.0,
    "rainfall": 202.93
  }'
```

**Success Response (200 OK):**
```json
{
    "crop": "Rice",
    "message": "Prediction successful"
}
```

**Validation Error Response (400 Bad Request):**
```json
{
    "error": "Validation failed",
    "fieldErrors": {
        "nitrogen": "Nitrogen content is required",
        "temperature": "Temperature is required"
    }
}
```

**ML Service Unavailable Response (503 Service Unavailable):**
```json
{
    "crop": null,
    "message": "ML Service is currently unavailable. Please try again later."
}
```

### 2. Health Check

**Request:**
```bash
curl http://localhost:8080/api/crop/health
```

**Response (200 OK):**
```json
{
    "status": "UP",
    "message": "Crop Prediction Service is running"
}
```

---

## ⚙️ Configuration

### Application Properties

File: `src/main/resources/application.properties`

```properties
# Server Configuration
server.port=8080

# Flask ML API URL
flask.api.url=http://localhost:5000/predict

# Disable DataSource (No Database)
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration

# Logging (Optional)
logging.level.com.crop.cropbackend=DEBUG
```

### Customizable Properties

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | `8080` | Application port |
| `flask.api.url` | `http://localhost:5000/predict` | Flask ML API endpoint |

---

## 🧪 Request Body Schema

### CropRequest

| Field | Type | Required | Validation | Description |
|-------|------|----------|------------|-------------|
| `nitrogen` | Double | Yes | ≥ 0 | Nitrogen content (mg/kg) |
| `phosphorus` | Double | Yes | ≥ 0 | Phosphorus content (mg/kg) |
| `potassium` | Double | Yes | ≥ 0 | Potassium content (mg/kg) |
| `temperature` | Double | Yes | - | Temperature (°C) |
| `humidity` | Double | Yes | ≥ 0 | Humidity (%) |
| `rainfall` | Double | Yes | ≥ 0 | Rainfall (mm) |

### Typical Value Ranges

| Parameter | Typical Range |
|-----------|---------------|
| Nitrogen | 0 - 140 mg/kg |
| Phosphorus | 5 - 145 mg/kg |
| Potassium | 5 - 205 mg/kg |
| Temperature | 8 - 45 °C |
| Humidity | 14 - 100 % |
| Rainfall | 20 - 300 mm |

---

## ⚠️ Error Handling

The application handles various error scenarios:

| Scenario | HTTP Status | Error Message |
|----------|-------------|---------------|
| Invalid input | 400 | Validation failed with field errors |
| Flask API unreachable | 503 | ML Service is currently unavailable |
| Flask API 4xx error | 503 | Invalid request to ML Service |
| Flask API 5xx error | 503 | ML Service encountered an error |
| Unexpected error | 500 | An unexpected error occurred |

---

## 🔗 Flask ML API Requirements

Your Flask ML API should:

1. Listen at `http://localhost:5000/predict`
2. Accept POST requests with JSON body
3. Return JSON response with `crop` field

**Expected Flask API Request:**
```json
{
    "nitrogen": 90,
    "phosphorus": 42,
    "potassium": 43,
    "temperature": 20.87,
    "humidity": 82.0,
    "rainfall": 202.93
}
```

**Expected Flask API Response:**
```json
{
    "crop": "Rice"
}
```

---

## 📜 License

This project is created for educational and demonstration purposes.

---

## 👨‍💻 Author

Smart Crop Recommendation System - Spring Boot Backend

---

*Happy Farming! 🌾🚜*
