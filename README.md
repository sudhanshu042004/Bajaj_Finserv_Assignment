# Bajaj Finserv Backend API

This is a Spring Boot application that implements the REST API for the Bajaj Finserv Health Challenge.

## Prerequisites
- Java 17
- Maven (optional, wrapper included/Docker used)
- Docker (optional, for containerized deployment)
- Google Gemini API Key

## Setup

1. **Clone the repository** (if not already done).
2. **Set the API Key**:
   You need to set the `GEMINI_API_KEY` environment variable for the AI integration to work.

   ```bash
   export GEMINI_API_KEY=your_api_key_here
   export OFFICIAL_EMAIL=your_email_here # Optional, defaults to sudhanshu1515.be23@chitkarauniversity.edu.in
   ```

## Running with Maven

```bash
mvn spring-boot:run
```

## Running with Docker

1. **Build the image**:
   ```bash
   docker build -t backend-api .
   ```

2. **Run the container** (passing the API Key):
   ```bash
   docker run -p 8080:8080 -e GEMINI_API_KEY=your_api_key_here backend-api
   ```

## Endpoints

### 1. GET /health
Returns the health status and official email.
```json
{
  "is_success": true,
  "official_email": "sudhanshu1515.be23@chitkarauniversity.edu.in"
}
```

### 2. POST /bfhl
Accepts JSON with one of the following keys: `fibonacci`, `prime`, `lcm`, `hcf`, `AI`.

**Example Request:**
```json
{
  "fibonacci": 7
}
```

**Example Response:**
```json
{
  "is_success": true,
  "official_email": "sudhanshu1515.be23@chitkarauniversity.edu.in",
  "data": [0, 1, 1, 2, 3, 5, 8]
}
```

## Deployment
This application is ready for deployment on Vercel, Railway, or Render via the provided Dockerfile.
