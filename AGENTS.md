# RailOpt-AI Backend — Agent Instructions

## 1. Project Identity

**Project:** RailOpt-AI

**SIH Problem Statement:**

**SIH26027 — AI-Powered Automatic Block Planning to Maximize Asset Availability for Train Operations on Indian Railways**

RailOpt-AI is a railway operations and automatic block-planning system intended to coordinate maintenance activities with train operations, infrastructure availability, assets, forecasts, resources, conflicts, and scheduling.

This repository contains the **Spring Boot backend**.

The frontend and AI/ML services are separate projects.

---

# 2. Repository Context

The overall project is organized as:

```text
RailOpt_AI/
│
├── Backend/
│   └── Spring Boot backend
│
├── Frontend/
│   └── React frontend
│
└── AI/
    └── Python AI/ML service
```

When working in this repository, focus on the **Backend** project.

Do not modify the Frontend or AI projects unless explicitly instructed.

---

# 3. Technology Stack

## Backend

* Java
* Spring Boot
* Spring REST
* Spring Data JPA
* PostgreSQL
* Firebase Authentication

## Frontend

* React
* Communicates with the backend through REST APIs

## AI/ML

* Python
* Separate service
* Communicates with Spring Boot through REST APIs

## Database

* PostgreSQL

## API Documentation

* OpenAPI 3.0.3
* YAML specification

---

# 4. High-Level Architecture

The system follows:

```text
React Frontend
       │
       │ REST / JSON
       ▼
Spring Boot Backend
       │
       ├──────────────► PostgreSQL
       │
       │ REST
       ▼
Python AI/ML Service
       │
       ▼
Spring Boot Backend
       │
       ▼
PostgreSQL
```

Spring Boot is the **main backend and application orchestrator**.

Python is a specialized AI/ML service.

Python must **not directly access PostgreSQL**.

---

# 5. Authoritative Project Documents

The following files are the primary sources of truth.

## Database Schema

```text
docs/database-schema.mmd
```

This defines:

* tables
* columns
* primary keys
* foreign keys
* relationships
* database structure

### Database Schema Management

PostgreSQL schema changes are managed using Flyway migrations.

Hibernate/JPA must not be used to create or modify the database schema.
Use Hibernate/JPA for entity mapping and schema validation.

## API Documentation

```text
docs/api.yaml
```

This defines:

* endpoints
* HTTP methods
* request parameters
* request bodies
* response structures
* authentication
* API tags

## Architecture

```text
ARCHITECTURE.md
```

This provides the high-level system architecture and data flow.

---

# 6. Source-of-Truth Priority

When implementing anything, follow this priority:

```text
1. Explicit user instruction
2. Final database schema
3. Final API specification
4. Architecture documentation
5. Existing backend implementation
6. General Spring Boot conventions
7. AI assumptions
```

If two sources appear inconsistent, **do not silently choose one**.

Identify the conflict and ask for clarification when it affects implementation.

---

# 7. Critical Schema-First Rule

**Never design backend APIs or database mappings independently of the finalized database schema.**

Before implementing a module:

```text
Database Schema
       ↓
API Specification
       ↓
Existing Backend Patterns
       ↓
Implementation
```

Always inspect the relevant section of:

```text
docs/database-schema.mmd
```

before creating or modifying:

* Entity
* Repository
* DTO
* Service
* Controller
* Database query
* API endpoint

Do not invent:

* tables
* columns
* relationships
* foreign keys
* enums
* API fields

If a required field does not exist in the schema, do not invent one.

---

# 8. API-First Contract Rule

The API documentation is already designed.

Use:

```text
docs/api.yaml
```

as the authoritative API contract.

Do not create new endpoints merely because they seem useful.

Do not rename existing endpoints.

Do not change:

* HTTP methods
* paths
* request fields
* response fields
* parameter names
* API tags

unless explicitly instructed.

The backend implementation must conform to the OpenAPI specification.

---

# 9. API Conventions

The project uses:

* REST APIs
* JSON request/response bodies
* OpenAPI 3.0.3
* generic paths
* UUID identifiers where defined
* Firebase Bearer authentication

The API does **not** use an `/api/v1` prefix unless explicitly introduced later.

Existing API modules/tags include:

```text
System
User
Organization
Infrastructure
Assets
Maintenance
Train Operations
Forecasting
Scheduling
Conflicts
Metrics
AI/ML
```

Reuse existing tags.

Do not create duplicate tags for the same domain.

---

# 10. Path Parameter Rules

Every OpenAPI path parameter must have a matching parameter definition.

For example:

```yaml
/resource/{resourceId}:
```

must have:

```yaml
- name: resourceId
  in: path
  required: true
```

Do not create:

* unmatched path parameters
* duplicate path definitions
* duplicate YAML keys
* malformed indentation
* conflicting API paths

Keep the OpenAPI document valid after modifications.

---

# 11. Database Metadata Rule

Do not expose every database column through the API.

In particular, do not automatically expose:

```text
created_at
updated_at
```

unless there is an explicit API/business requirement.

Database structure and API structure are separate concerns.

A database field should become an API field only when it is useful and required by the API contract.

---

# 12. Backend Architecture

Use a conventional Spring Boot layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

### Controller

Responsible for:

* HTTP requests
* request validation
* authentication context
* HTTP responses

Controllers should remain thin.

### Service

Responsible for:

* business logic
* orchestration
* validation requiring business rules
* coordination between repositories/services
* communication with Python AI/ML when required

### Repository

Responsible for:

* persistence
* database queries
* Spring Data/JPA operations

Do not put application/business orchestration inside repositories.

---

# 13. DTO and Entity Separation

Do not expose JPA entities directly as API contracts unless explicitly required.

Prefer:

```text
Request DTO
     ↓
Controller
     ↓
Service
     ↓
Entity
     ↓
Repository
```

and:

```text
Repository
     ↓
Entity
     ↓
Service
     ↓
Response DTO
     ↓
Controller
```

The API contract should not automatically mirror the database entity.

---

# 14. Dependency Injection

Use constructor injection.

Prefer:

```java
public ExampleService(ExampleRepository repository) {
    this.repository = repository;
}
```

Avoid unnecessary field injection.

Follow the existing project style if a different established pattern already exists.

---

# 15. Authentication

Authentication uses **Firebase Authentication**.

The frontend sends:

```http
Authorization: Bearer <firebase-id-token>
```

Spring Boot verifies the Firebase ID token.

The Firebase UID is used to identify the authenticated user.

Do not create a separate custom password authentication system.

Do not store Firebase passwords.

Do not replace Firebase Authentication unless explicitly instructed.

---

# 16. Main Backend Modules

The finalized backend API is organized into:

```text
1. System
2. User
3. Organization
4. Infrastructure
5. Assets
6. Maintenance
7. Train Operations
8. Forecasting
9. Scheduling
10. Conflicts
11. Metrics
12. AI/ML
```

The first eleven are database-backed operational modules.

AI/ML is treated differently because it is implemented as a separate Python service.

---

# 17. Core Railway Domain

The backend represents several connected railway domains:

```text
Organization
      │
      ▼
Infrastructure
      │
      ├── Stations
      ├── Track Sections
      └── Block Sections
              │
              ├───────────────┐
              ▼               ▼
            Assets       Train Operations
              │               │
              ▼               ▼
         Maintenance       Train Runs
              │               │
              └───────┬───────┘
                      ▼
                  Scheduling
                      │
          ┌───────────┼───────────┐
          ▼           ▼           ▼
       Blocks      Conflicts     Metrics
```

Traffic forecasting provides additional operational input to scheduling.

---

# 18. Train Operations

The finalized train-operation domain includes entities for:

* train types
* trains
* train routes
* route stops
* train schedules
* train runs
* run stops
* run track sections
* run block sections

The general relationship is:

```text
TRAIN
 ├── TRAIN_TYPE
 ├── TRAIN_ROUTE
 │      └── ROUTE_STOPS → STATION
 │
 └── TRAIN_SCHEDULE
        └── TRAIN_RUN
              ├── RUN_STOPS → STATION
              ├── RUN_TRACK_SECTIONS → TRACK_SECTION
              └── RUN_BLOCK_SECTIONS → BLOCK_SECTION
```

Always verify exact relationships against:

```text
docs/database-schema.mmd
```

---

# 19. Forecasting

The forecasting domain contains:

```text
TRAFFIC_FORECASTS
TRAFFIC_FORECAST_ITEMS
```

Forecasts are associated with railway divisions.

Forecast items are associated with block sections and time windows.

Forecasting provides information that can be used by scheduling.

Do not assume that forecasting automatically means a separate AI/ML database model.

---

# 20. Scheduling

Scheduling is the central planning domain.

The finalized scheduling domain includes:

```text
SCHEDULING_RUNS
SCHEDULING_RUN_TASKS
SCHEDULED_TASKS
SCHEDULED_BLOCKS
SCHEDULED_BLOCK_RESOURCES
```

Conceptually:

```text
Scheduling Run
      │
      ├── Tasks
      │
      ├── Scheduled Tasks
      │
      └── Scheduled Blocks
              │
              └── Block Resources
```

A scheduling run represents an execution of the planning process.

The resulting schedule represents the coordinated maintenance/block plan.

---

# 21. Conflict Management

Conflicts are represented by:

```text
SCHEDULE_CONFLICTS
```

Conflicts can relate to scheduling, train operations, resources, tracks, and block sections according to the finalized database relationships.

Do not invent conflict relationships or additional tables.

---

# 22. Metrics

Scheduling results are represented through:

```text
SCHEDULE_METRICS
SCHEDULE_BASELINE_METRICS
```

Metrics can represent scheduling outcomes such as:

* task scheduling
* block utilization
* conflicts
* train delay impact
* asset downtime
* asset availability
* resource utilization
* maintenance completion
* optimization objective results

Do not hard-code target numbers from presentation/prototype material as actual business values.

---

# 23. Optimization Objective

The core business purpose of RailOpt-AI is to coordinate maintenance block planning while considering:

* maintenance requirements
* train operations
* infrastructure availability
* block sections
* assets
* available resources
* traffic forecasts
* scheduling conflicts

The conceptual optimization flow is:

```text
Maintenance Requests
        +
Train Operations
        +
Infrastructure Availability
        +
Traffic Forecasts
        +
Resources
        ↓
Conflict Detection
        ↓
Activity Prioritization
        ↓
Compatible Activity Grouping
        ↓
Schedule Optimization
        ↓
Optimized Blocks
        ↓
Conflict Evaluation
        ↓
Scheduling Metrics
```

The system aims to:

* maximize asset availability
* reduce train disruption
* reduce scheduling conflicts
* improve maintenance efficiency
* coordinate compatible maintenance activities into shared blocks

These are **system objectives**, not guaranteed numerical results.

---

# 24. AI/ML Architecture

AI/ML is implemented separately in Python.

Spring Boot communicates with the Python service through REST APIs.

The intended flow is:

```text
React
   ↓
Spring Boot
   ↓
Collect required operational data
   ↓
Python AI/ML Service
   ↓
Prediction / Prioritization / Optimization
   ↓
Spring Boot
   ↓
Validate and process result
   ↓
Persist final operational result
   ↓
PostgreSQL
```

Python must not directly access PostgreSQL.

Spring Boot remains responsible for:

* API orchestration
* business validation
* database access
* persistence
* integration with the rest of the application

Python is responsible for:

* ML models
* prediction
* optimization algorithms
* AI/ML-specific processing

---

# 25. AI/ML Persistence

There is currently no requirement for generic tables such as:

```text
AI_MODEL_RESULTS
ML_PREDICTIONS
AI_RECOMMENDATIONS
MODEL_RUNS
```

Do not create these tables merely because the project uses AI.

The final operational results can be stored using the existing scheduling/conflict/metrics tables where appropriate.

If model history, versioning, auditability, or persistent recommendations are later required, that should be an explicit database design decision.

---

# 26. Prototype vs Main Project

A separate prototype/PRD exists for demonstration purposes.

Prototype documents may contain:

* simplified data models
* mocked optimization
* simulated live data
* prototype-only screens
* demo mode
* simplified APIs
* temporary algorithms

These are **not authoritative for the main backend**.

The main backend must follow:

```text
docs/database-schema.mmd
docs/api.yaml
```

The prototype may be used only to understand:

* product intent
* user personas
* workflow
* UI expectations
* optimization concept
* demonstration flow

Do not import prototype-specific architecture such as FastAPI or simplified prototype tables into the Spring Boot backend.

---

# 27. No Unnecessary Architecture Changes

Do not:

* replace Spring Boot
* introduce FastAPI into the backend
* replace PostgreSQL
* replace Firebase Authentication
* move AI/ML logic into Java
* move database access into Python
* redesign the database
* redesign the API
* introduce microservices unnecessarily

unless explicitly instructed.

---

# 28. Minimal-Change Principle

When implementing a feature:

**Make the smallest correct change.**

Do not automatically:

* refactor unrelated modules
* rename packages
* rename classes
* replace libraries
* rewrite working code
* change API contracts
* modify unrelated database mappings
* add unnecessary dependencies

If a refactor would be beneficial but is not necessary, mention it separately instead of performing it automatically.

---

# 29. Repository Exploration

Do not read the entire repository unnecessarily.

For a module, search for and inspect only the relevant:

```text
Controller
Service
Repository
Entity
DTO
Configuration
Tests
```

Also inspect the relevant portions of:

```text
docs/database-schema.mmd
docs/api.yaml
```

Prefer targeted repository search over reading large unrelated files.

---

# 30. Implementation Workflow

For every meaningful implementation request:

```text
1. Understand the requested feature
       ↓
2. Locate existing related code
       ↓
3. Inspect database schema
       ↓
4. Inspect API specification
       ↓
5. Inspect existing implementation patterns
       ↓
6. Create implementation plan
       ↓
7. Implement
       ↓
8. Compile
       ↓
9. Run relevant tests
       ↓
10. Review changed files
       ↓
11. Report result
```

For large changes, present the plan before implementation.

---

# 31. Do Not Invent

The AI agent must never silently invent:

```text
Database tables
Database fields
Foreign keys
API endpoints
API parameters
API response fields
Business rules
Authentication mechanisms
External integrations
AI/ML behavior
```

If the requirement is missing:

1. Search the repository.
2. Check the schema.
3. Check the API specification.
4. Check the architecture.
5. Ask the user if still unclear.

---

# 32. Code Quality

Prefer:

* clear class names
* small focused classes
* constructor injection
* meaningful method names
* validation
* reusable services
* appropriate exception handling
* testable business logic

Avoid:

* giant controllers
* giant service classes
* duplicated logic
* unnecessary abstractions
* unnecessary design patterns
* hard-coded business data
* hard-coded credentials
* hidden side effects

Do not over-engineer a prototype feature into a production framework unless explicitly requested.

---

# 33. Configuration and Secrets

Never hard-code:

* passwords
* API keys
* Firebase credentials
* access tokens
* database credentials
* private keys

Use environment variables or the existing configuration mechanism.

Never commit secrets to Git.

---

# 34. Git Safety

Before modifying significant code:

* inspect Git status
* preserve existing user changes
* do not overwrite unrelated work

Never run destructive Git commands such as reset/revert/clean to remove user work unless explicitly instructed.

Keep changes focused.

---

# 35. Verification

After implementation, verify where applicable:

```text
✓ Project compiles
✓ Spring application starts
✓ Relevant tests pass
✓ API mappings are valid
✓ DTOs match API specification
✓ Entity mappings match database schema
✓ No duplicate endpoints
✓ No unintended files changed
```

For an API feature, verify:

```text
API specification
       ↕
Controller
       ↕
DTO
       ↕
Service
       ↕
Repository
       ↕
Database schema
```

If something does not match, report it.

Do not silently modify the specification to make the implementation easier.

---

# 36. Response Format for Development Tasks

After completing a task, provide a concise summary:

```text
Implemented:
- ...

Files created:
- ...

Files modified:
- ...

Database tables involved:
- ...

APIs implemented:
- ...

Verification:
- ...

Issues / remaining work:
- ...
```

Do not paste entire files unless explicitly requested.

---

# 37. Final Agent Rule

The architecture and contracts of RailOpt-AI have already been designed.

The AI agent's job is to:

```text
Understand
   ↓
Locate
   ↓
Plan
   ↓
Implement
   ↓
Test
   ↓
Verify
```

The AI agent is **not** the project architect.

When uncertain:

```text
Search the repository.
Check the database schema.
Check the API specification.
Check the architecture.
Ask instead of inventing.
```
