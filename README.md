# Task Manager API

## Overview

A simple CRUD task Spring-Boot backend with following endpoints. This project is a Task Manager API built with Spring Boot 3.5+, Java 21, MySQL 8+, and Docker. It supports basic task management with full CRUD operations.

## API implementation.

## Features

- CRUD APIs for tasks
- Swagger UI documentation at `/swagger-ui.html`
- CI/CD by github action
- Gemini code review bot
- Unit and integration tests
- Dockerized setup

## Architecture
The project is structured into:

- **Controller** – defines API endpoints and validates requests
- **Service** – contains business logic
- **Mapper** – converts between entities and response models
- **Repository** – handles database access via Spring Data JPA

## Prerequisites
- Java 21
- Docker & Docker Compose
- MySQL 8+

And all the dependencies for Spring Boot is already defined in gradle

## Quick Start
```bash
# Single command to run the entire application
./run.sh
```

## API Documentation
- **OpenAPI Specification**: Available at `/swagger-ui.html` when running
- **Postman Collection**: Import `postman_collection.json` for testing

## Testing
Model Task:
| Field    | Type |
| -------- | ------- |
| id  | Primary Key    |
| title | String     |
| description    | String    |
| createdAt    | OffsetDateTime  |
| updatedAt | OffsetDateTime |


### Unit Tests
```bash
./gradlew test
```

### Integration Tests
```bash
./gradlew integrationTest
```

## Database Schema
Describe your database structure and any migration approach used.

## Observability
- **Metrics**: Available at `/actuator/prometheus`
- **Grafana Dashboard**: Access at `http://localhost:3000`
- **Prometheus**: Access at `http://localhost:9090`

## CI/CD Pipeline
- Pull Requests: Each pull request triggers a GitHub Actions workflow that runs tests with Gradle, collects code coverage using JaCoCo, and uploads the coverage report to Codecov.

- Main Branch Merges: When changes are merged into the main branch, the CI pipeline runs again via GitHub Actions to re-run all tests and update the code coverage metrics on Codecov.

- Releases: When a new GitHub release is published, the pipeline builds a Docker image of the application and pushes it to the GitHub Container Registry (GHCR) with the appropriate tags for that release.

## Assumptions Made
List any assumptions you made during development:
- I assume authorization is excluded. If we add authorization, we'll also have to define the unauthorized responses in the OpenAPI spec, which I'd like to avoid.

## Known Limitations
List any known limitations or areas for improvement:
- The repository now only have master branch as PROD environemnt. To sepearate the env we can create another develop branch specifically for testing
- The app now does not support authorization. That is vulnerable for attacking, because the hacker can just do a for loop to call api, the database will be flood with dummy data
- 

## Technology Stack
- Spring Boot 3.5+
- Java 21
- MySQL 8+
- Docker & Docker Compose

## Author
Dang Huy Phuong - pdanghuy03@gmail.com
