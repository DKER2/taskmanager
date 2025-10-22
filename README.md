# Task Manager API

## Overview

A simple CRUD task Spring-Boot backend with following endpoints

## API implementation.

## Architecture
I architecture application to three layer. `Controller` where the routing happen and validate the request. `Service` is where logic of application is mainly defined.
`Mapper` as a mapping layer between Entity and Response contract.
`Reposiory` is where an abstraction layer of database, instead of wrting SQL query from scratch, the repository will auto generate some default query

## Prerequisites
Java 21
Docker

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
Describe your testing strategy and how to run tests.

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

## Observability (if implemented)
- **Metrics**: Available at `/actuator/prometheus`
- **Grafana Dashboard**: Access at `http://localhost:3000`
- **Prometheus**: Access at `http://localhost:9090`

## CI/CD Pipeline
Describe your continuous integration setup and deployment strategy.

## Assumptions Made
List any assumptions you made during development:
- I assume authorization is excluded. If we add authorization, we'll also have to define the unauthorized responses in the OpenAPI spec, which I'd like to avoid.
- 
- 

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
- [Any additional technologies used]

## Author
Your Name - [Your Email]
