# Incident Classifier

A **Spring Boot** application that classifies incident documents and extracts topics. It exposes a simple REST API for uploading PDFs, retrieving classified results, and getting dashboard statistics.

## Prerequisites
- Java 17 (or compatible JDK)
- Maven (the repository includes the Maven Wrapper, so you can use it without installing Maven globally)

## Build & Run
```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

After the application starts, it will listen on `http://localhost:8080`.

## API Overview
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST`  | `/api/topics` | Create topics |
| `GET`  | `/api/topics` | List all topics |
| `POST` | `/api/documents/text` | Provide text for classification |
| `POST` | `/api/documents` | Upload a PDF document for classification |
| `GET`  | `/api/documents/{id}` | Retrieve classification results for a specific document |

## Testing with Postman
A Postman collection is provided to exercise the API. You can import the collection using the following placeholder URL:

```
https://www.postman.com
```//More information in the file /Incident Classifier APIs.postman_collection.json


