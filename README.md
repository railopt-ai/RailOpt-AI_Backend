# RailOpt-AI Backend

Spring Boot backend for the SIH26027 solution:

**AI-Powered Automatic Block Planning to Maximize Asset Availability for Train Operations on Indian Railways**

---

##  Backend Overview

This repository contains the **core backend** of the application.

The backend is responsible for:

- REST APIs
- Business logic
- Database operations
- Data validation
- Maintenance/task management
- Railway and train-related data management
- Block and planning management
- Authentication/authorization
- Communication with the Python AI/Optimization Engine

The system is divided into three main applications:

```text
Frontend
   │
   │ REST API
   ▼
Spring Boot Backend
   │
   ├── PostgreSQL
   │
   └── Python AI/Optimization Engine
```

The Spring Boot backend acts as the **main application layer and system-of-record**.

---

##  Tech Stack

### Backend

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Bean Validation
- Maven

### Database

- PostgreSQL
- Flyway

### AI Integration

- Python AI/Optimization Engine
- REST/JSON communication

### Development

- Git
- GitHub
- Postman
- IntelliJ IDEA / VS Code

---

##  Architecture

```text
                    ┌─────────────────────┐
                    │      Frontend       │
                    │        React        │
                    └──────────┬──────────┘
                               │
                            REST API
                               │
                               ▼
              ┌────────────────────────────────┐
              │       Spring Boot Backend      │
              │                                │
              │  Controllers                   │
              │  DTOs                          │
              │  Services                      │
              │  Repositories                  │
              │  Domain / Entities             │
              │  Validation                    │
              │  AI Integration                │
              └───────────────┬────────────────┘
                              │
                  ┌───────────┴───────────┐
                  │                       │
                  ▼                       ▼
             PostgreSQL            Python AI Engine
                                      │
                                      ▼
                                Optimization
```

### Responsibility Separation

```text
Frontend
    → UI and visualization

Spring Boot
    → APIs, business logic, persistence, orchestration

Python AI Engine
    → Optimization / scheduling computation

PostgreSQL
    → Persistent application data
```

---

##  Project Structure

```text
sih26027-backend/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/<team>/<project>/
│   │   │       │
│   │   │       ├── config/
│   │   │       │
│   │   │       ├── controller/
│   │   │       │
│   │   │       ├── dto/
│   │   │       │   ├── maintenance/
│   │   │       │   ├── railway/
│   │   │       │   ├── train/
│   │   │       │   ├── block/
│   │   │       │   └── planning/
│   │   │       │
│   │   │       ├── entity/
│   │   │       │
│   │   │       ├── repository/
│   │   │       │
│   │   │       ├── service/
│   │   │       │
│   │   │       ├── integration/
│   │   │       │   └── ai/
│   │   │       │
│   │   │       ├── exception/
│   │   │       │
│   │   │       └── util/
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/
│   │           └── migration/
│   │
│   └── test/
│
├── docs/
│   └── api/
│
├── .env.example
├── .gitignore
├── pom.xml
└── README.md
```

---

##  Layered Backend Structure

The backend follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

With DTOs separating the API from persistence:

```text
HTTP Request
     ↓
Request DTO
     ↓
Controller
     ↓
Service
     ↓
Entity
     ↓
Repository
     ↓
PostgreSQL
```

Response:

```text
PostgreSQL
     ↓
Repository
     ↓
Entity
     ↓
Service
     ↓
Response DTO
     ↓
Controller
     ↓
HTTP Response
```

### Controllers

Handle:

- HTTP requests
- Request parameters
- Request/response DTOs
- HTTP status codes

Controllers should contain minimal business logic.

### Services

Handle:

- Business logic
- Validation beyond basic request validation
- Workflows
- Transaction boundaries
- AI-engine orchestration

### Repositories

Handle database access using Spring Data JPA.

### Entities

Represent persistent domain objects.

### DTOs

Represent API contracts.

Entities should not be exposed directly through the REST API.

---

##  Main Backend Modules

### Railway

Responsible for railway-related data.

Potential components:

```text
Station
RailwaySection
```

### Train

Responsible for train and movement information.

```text
Train
TrainMovement
```

### Maintenance

Responsible for maintenance requirements.

```text
Department
MaintenanceTask
MaintenanceResource
```

### Block

Responsible for block-related information.

```text
Block
BlockActivity
```

### Planning

Responsible for planning operations.

```text
BlockPlan
PlanningRequest
PlanningResult
```

### AI Integration

Responsible for communication with the Python engine.

```text
AiEngineClient
OptimizationRequest
OptimizationResponse
```

---

##  Database

### Database

```text
PostgreSQL
```

### Migration

Database schema changes are managed using **Flyway**.

Migration files:

```text
src/main/resources/db/migration/
```

Example:

```text
V1__create_station_table.sql
V2__create_railway_section_table.sql
V3__create_train_table.sql
```

Migration files should be versioned and committed to Git.

---

##  API Structure

The backend exposes versioned REST APIs.

Initial API grouping:

```text
/api/v1/stations

/api/v1/sections

/api/v1/trains
/api/v1/train-movements

/api/v1/departments
/api/v1/maintenance-tasks
/api/v1/resources

/api/v1/blocks

/api/v1/plans
/api/v1/plans/{id}

/api/v1/plans/generate
/api/v1/plans/{id}/reoptimize
```

The exact endpoints will evolve as the domain model and requirements are finalized.

---

##  API Design

APIs should be designed as contracts between the frontend and backend.

Every endpoint should define:

- HTTP method
- URL
- Request parameters
- Request body
- Response body
- Validation rules
- HTTP status codes
- Error response

Example:

```http
POST /api/v1/maintenance-tasks
```

Request:

```json
{
  "sectionId": "SEC-001",
  "durationMinutes": 120,
  "priority": 80
}
```

Response:

```json
{
  "id": "MT-001",
  "sectionId": "SEC-001",
  "durationMinutes": 120,
  "priority": 80,
  "status": "PENDING"
}
```

Actual fields will be finalized with the domain model.

---

##  AI Engine Integration

The AI/Optimization Engine is a separate Python application.

The Spring Boot backend communicates with it through REST APIs.

```text
Spring Boot
     │
     │ OptimizationRequest
     ▼
Python AI Engine
     │
     │ Optimization
     ▼
OptimizationResponse
     │
     ▼
Spring Boot
```

### Backend Responsibilities

The backend should:

1. Receive a planning request.
2. Load required data.
3. Validate the request.
4. Construct an `OptimizationRequest`.
5. Send it to the AI engine.
6. Receive the result.
7. Validate the result.
8. Persist the resulting plan.
9. Return the plan to the frontend.

### Important Separation

The backend should **not contain the optimization algorithm itself**.

The Python service owns the computational optimization logic.

The backend owns the application/domain workflow around it.

---

##  AI Integration Package

Suggested structure:

```text
integration/
└── ai/
    ├── AiEngineClient.java
    ├── OptimizationRequest.java
    ├── OptimizationResponse.java
    └── AiEngineException.java
```

Example responsibility:

```java
public interface AiEngineClient {

    OptimizationResponse optimize(
        OptimizationRequest request
    );
}
```

The implementation can use an HTTP client such as:

- Spring `RestClient`
- Spring `WebClient`

The selected client will depend on the final project requirements.

---

## 12. Planning Request Flow

```text
Frontend
    │
    │ POST /api/v1/plans/generate
    ▼
PlanningController
    │
    ▼
PlanningService
    │
    ├── Load data
    ├── Validate data
    └── Build OptimizationRequest
              │
              ▼
       AiEngineClient
              │
              ▼
       Python AI Engine
              │
              ▼
      OptimizationResponse
              │
              ▼
       PlanningService
              │
              ├── Validate result
              ├── Save plan
              └── Calculate required response
              │
              ▼
        Response DTO
              │
              ▼
          Frontend
```

---

##  Validation

Use Jakarta Bean Validation for request-level validation.

Example:

```java
@NotNull
private String sectionId;

@Positive
private int durationMinutes;
```

Business validation belongs in the service layer.

Examples:

- Section must exist.
- Maintenance duration must be valid.
- Referenced resources must exist.
- Time ranges must be valid.
- A planning request must contain valid data.

---

## 14. Error Handling

Use centralized exception handling.

Suggested structure:

```text
exception/
├── GlobalExceptionHandler.java
├── ResourceNotFoundException.java
├── ValidationException.java
├── ConflictException.java
└── AiEngineException.java
```

Example response:

```json
{
  "timestamp": "2026-09-01T10:30:00Z",
  "status": 404,
  "error": "RESOURCE_NOT_FOUND",
  "message": "Railway section not found",
  "path": "/api/v1/sections/SEC-001"
}
```

---

##  Configuration

Environment-specific values should not be hardcoded.

Example:

```text
DB_HOST=localhost
DB_PORT=5432
DB_NAME=sih26027
DB_USERNAME=postgres
DB_PASSWORD=

AI_ENGINE_URL=http://localhost:8000
```

Use:

```text
.env.example
```

as a template.

Never commit real credentials or secrets.

---

##  Local Setup

### Requirements

Install:

- Java
- Maven
- PostgreSQL
- Git

Optional:

- Docker
- Postman

Verify:

```bash
java -version
mvn -version
psql --version
git --version
```

### Clone

```bash
git clone <BACKEND_REPOSITORY_URL>
cd sih26027-backend
```

### Database

Create the database:

```sql
CREATE DATABASE sih26027;
```

Configure the application using environment variables.

### Run

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

---

##  Testing

Run:

```bash
./mvnw test
```

Windows:

```bash
mvnw.cmd test
```

Testing should cover:

- Controller tests
- Service tests
- Validation
- Repository behavior where required
- AI integration
- Planning workflow
- Exception handling

---

##  API Documentation

API documentation should be maintained separately from implementation details.

Recommended:

```text
docs/
└── api/
```

The backend may also use OpenAPI/Swagger for interactive API documentation.

The API contract should be finalized before frontend/backend integration wherever practical.

---

##  Git Structure

Recommended branches:

```text
main
develop

feature/maintenance-task
feature/train-management
feature/block-management
feature/planning-api
feature/ai-integration
feature/authentication
```

Example commits:

```text
feat: add maintenance task API
feat: implement block plan entity
fix: validate maintenance duration
docs: update planning API
```

Avoid committing unfinished work directly to `main`.

---

##  Development Order

The backend will generally be developed in this order:

```text
Requirements
     ↓
Domain Model
     ↓
Database Design
     ↓
Entities
     ↓
Repositories
     ↓
DTOs
     ↓
Services
     ↓
Controllers
     ↓
API Documentation
     ↓
AI Integration
     ↓
Testing
     ↓
Frontend Integration
```

The API contract acts as the boundary between the backend and frontend.

---

##  Current Development Status

```text
[ ] Requirements finalized
[ ] Domain model finalized
[ ] Database schema finalized
[ ] Flyway migrations
[ ] Core entities
[ ] Repositories
[ ] DTOs
[ ] Services
[ ] REST APIs
[ ] API documentation
[ ] Authentication
[ ] AI engine integration
[ ] Planning workflow
[ ] Replanning
[ ] Unit tests
[ ] Integration tests
[ ] Frontend integration
```

---

##  Repository Rule

This repository contains the **Core Backend only**.

Do not place:

- React/frontend code
- Python optimization code
- ML models
- Training notebooks

inside this repository.

Those belong to their respective repositories.

```text
SIH26027-frontend
        │
        │ REST
        ▼
SIH26027-backend
        │
        │ REST
        ▼
SIH26027-ai-engine
```

The repositories should communicate through clearly defined interfaces and data contracts.
