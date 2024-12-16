# Flowchart Management System

This project is a Spring Boot application that manages flowcharts with basic CRUD operations and additional features.

## Setup

1. Clone the repository
2. Configure MySQL database settings in `src/main/resources/application.properties`
3. Run `mvn clean install` to build the project
4. Run `mvn spring-boot:run` to start the application

## API Documentation

Once the application is running, you can access the Swagger UI at:

http://localhost:8080/swagger-ui.html

This provides an interactive interface to explore and test the API endpoints.

## Features

- Create, Read, Update, and Delete flowcharts
- Validate flowchart graph structure
- Get outgoing edges for a specific node
- Find all nodes connected to a specific node

## Testing

Run `mvn test` to execute the unit tests.

## Technologies Used

- Java 11
- Spring Boot
- Spring Data JPA
- MySQL
- Lombok
- Swagger (SpringDoc OpenAPI)
- JUnit 5
