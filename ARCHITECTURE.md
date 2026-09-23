# RailOpt-AI Backend Architecture

## 1. Project Overview

**Project:** RailOpt-AI

**SIH Problem Statement:**

> SIH26027 — AI-Powered Automatic Block Planning to Maximize Asset Availability for Train Operations on Indian Railways

RailOpt-AI is a railway operations and automatic block-planning system.

The system coordinates maintenance activities with train operations, railway infrastructure, asset availability, traffic forecasts, resources, and scheduling constraints.

The primary objective is to generate coordinated maintenance block plans that improve asset availability while reducing disruption to train operations.

---

# 2. Overall System

RailOpt-AI consists of three independent applications:

    RailOpt_AI/

    │
    ├── Backend/
    │   └── Spring Boot + Java
    │
    ├── Frontend/
    │   └── React
    │
    └── AI/
        └── Python AI/ML Service

The high-level architecture is:

                         ┌───────────────────┐
                         │      React        │
                         │     Frontend      │
                         └─────────┬─────────┘
                                   │
                              REST / JSON
                                   │
                                   ▼
                         ┌───────────────────┐
                         │    Spring Boot    │
                         │      Backend      │
                         └───────┬─────┬─────┘
                                 │     │
                    ┌────────────┘     └──────────────┐
                    │                                 │
                    ▼                                 ▼
          ┌──────────────────┐              ┌──────────────────┐
          │    PostgreSQL    │              │  Python AI/ML    │
          │     Database     │              │     Service      │
          └──────────────────┘              └────────┬─────────┘
                                                     │
                                                     │ Result
                                                     ▼
                                            ┌──────────────────┐
                                            │   Spring Boot    │
                                            │   Validation &   │
                                            │   Orchestration  │
                                            └──────────────────┘

Spring Boot is the central application backend.

---

# 3. Component Responsibilities

## 3.1 React Frontend

The frontend is responsible for:

- user interface
- dashboards
- forms
- scheduling visualization
- Gantt/timeline interfaces
- conflict visualization
- analytics
- simulation interfaces
- live operational views
- user interaction

The frontend communicates with Spring Boot through REST APIs.

The frontend does not directly access PostgreSQL.

The frontend does not directly communicate with the Python AI/ML service for core application workflows.

---

## 3.2 Spring Boot Backend

Spring Boot is the main application backend and orchestrator.

Responsibilities include:

- REST APIs
- Firebase authentication verification
- application-level authorization/context
- business logic
- database access
- railway operational data management
- maintenance management
- train operations
- forecasting
- scheduling
- conflict management
- metrics
- AI/ML service integration
- validation and persistence of AI/optimization results

Spring Boot owns the application's persistent state.

---

## 3.3 Python AI/ML Service

Python is a separate service responsible for AI/ML and optimization-specific processing.

Potential responsibilities include:

- prediction
- prioritization
- optimization
- scheduling calculations
- AI/ML-specific analysis
- optimization recommendations

The Python service does not directly access PostgreSQL.

It receives the required input from Spring Boot and returns results to Spring Boot.

---

## 3.4 PostgreSQL

PostgreSQL is the persistent data store.

It stores the application's railway operational and scheduling data.

Spring Boot is responsible for database access.

Python does not directly access the database.

---

# 4. Request and Data Flow

## 4.1 Normal Application Request

The normal request flow is:

    React
      │
      │ HTTP REST request
      ▼
    Spring Boot Controller
      │
      ▼
    Service
      │
      ▼
    Repository
      │
      ▼
    PostgreSQL
      │
      ▼
    Repository
      │
      ▼
    Service
      │
      ▼
    Controller
      │
      ▼
    React

---

## 4.2 Authenticated Request

Authentication uses Firebase Authentication.

    React
      │
      │ Firebase authentication
      ▼
    Firebase
      │
      │ ID Token
      ▼
    React
      │
      │ Authorization: Bearer <firebase-id-token>
      ▼
    Spring Boot
      │
      │ Verify token
      ▼
    Authenticated Request

The Firebase UID is used to identify the authenticated application user.

Firebase authentication and application user data are separate concerns.

---

# 5. AI/ML Request Flow

AI/ML is integrated through Spring Boot.

The intended flow is:

    React
      │
      ▼
    Spring Boot
      │
      │ Load required operational data
      ▼
    Python AI/ML Service
      │
      ├── Analyze
      ├── Predict
      ├── Prioritize
      └── Optimize
      │
      ▼
    Optimization / Prediction Result
      │
      ▼
    Spring Boot
      │
      ├── Validate
      ├── Process
      └── Persist
      │
      ▼
    PostgreSQL
      │
      ▼
    React

The Python service is therefore a specialized computation service, not the main application backend.

---

# 6. Core Business Flow

The core planning process can be represented as:

    Maintenance Requirements
              +
    Train Operations
              +
    Infrastructure Availability
              +
    Asset Availability
              +
    Traffic Forecasts
              +
    Available Resources
              │
              ▼
       Conflict Detection
              │
              ▼
      Activity Prioritization
              │
              ▼
    Compatible Activity Grouping
              │
              ▼
       Schedule Optimization
              │
              ▼
        Optimized Block Plan
              │
              ├───────────────┐
              ▼               ▼
         Conflicts         Metrics
              │               │
              └───────┬───────┘
                      ▼
              Final Schedule

The purpose is to coordinate compatible maintenance activities into suitable blocks while considering train operations and available infrastructure.

---

# 7. Backend Layer Architecture

The backend follows a conventional layered architecture:

    ┌─────────────────────────────┐
    │          Controller         │
    │      HTTP / REST layer      │
    └──────────────┬──────────────┘
                   │
                   ▼
    ┌─────────────────────────────┐
    │           Service           │
    │        Business logic       │
    └──────────────┬──────────────┘
                   │
                   ├──────────────► Python AI/ML
                   │
                   ▼
    ┌─────────────────────────────┐
    │         Repository          │
    │       Persistence layer     │
    └──────────────┬──────────────┘
                   │
                   ▼
    ┌─────────────────────────────┐
    │         PostgreSQL          │
    └─────────────────────────────┘

### Controller

Handles:

- HTTP requests
- request/response mapping
- API validation
- authentication context

### Service

Handles:

- business rules
- application workflows
- orchestration
- cross-domain operations
- AI/ML service communication

### Repository

Handles:

- database operations
- persistence
- queries

### Entity

Represents the persistence model.

### DTO

Represents API request and response contracts.

---

# 8. Database Architecture

The database is PostgreSQL.

The authoritative schema is:

    docs/database-schema.mmd

The backend should treat the schema as the source of truth for:

- tables
- columns
- relationships
- primary keys
- foreign keys

The API layer does not necessarily expose every database field.

The database model and API model are intentionally separate.

## 8.1 Database Schema Management

Flyway is responsible for database schema creation and evolution.

All database schema changes must be introduced through version-controlled Flyway migrations.

Example migration structure:

    src/main/resources/
    └── db/
        └── migration/
            ├── V1__initial_schema.sql
            ├── V2__add_forecasting_tables.sql
            ├── V3__add_scheduling_tables.sql
            └── V4__add_conflict_tables.sql

Hibernate/JPA is responsible for:

- entity mapping
- persistence
- database interaction
- schema validation

Hibernate/JPA must not be used to create or modify the database schema.

The recommended configuration is:

    spring:
      jpa:
        hibernate:
          ddl-auto: validate

      flyway:
        enabled: true

The intended responsibility is:

    database-schema.mmd
            │
            ▼
       Flyway Migration
            │
            ▼
        PostgreSQL
            ▲
            │
       Hibernate/JPA
            │
            ▼
       Spring Boot

When the database schema changes:

1. Update the database design/source of truth.
2. Create a new Flyway migration.
3. Update the corresponding JPA entities.
4. Validate the entity/schema mapping.
5. Test the application.

Do not use:

    ddl-auto=update
    ddl-auto=create
    ddl-auto=create-drop

for normal project development.

Database migrations must be incremental and must not silently destroy existing data.

---

# 9. Domain Architecture

The backend is divided into the following functional domains:

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

The domains are related but should remain logically separated.

---

# 10. Organization and Infrastructure

The organizational structure provides context for railway operations.

Infrastructure represents the physical railway network.

Conceptually:

    Organization
        │
        ▼
    Railway Structure
        │
        ├── Stations
        ├── Track Sections
        └── Block Sections

Track and block information provides the physical context required by maintenance and train operations.

---

# 11. Assets and Maintenance

Assets are associated with railway infrastructure.

Maintenance activities operate on relevant assets/infrastructure.

Conceptually:

    Infrastructure
          │
          ▼
        Assets
          │
          ▼
      Maintenance

Maintenance information becomes one of the inputs to scheduling.

---

# 12. Train Operations

Train operations represent the movement and planning context against which maintenance blocks must be scheduled.

The finalized train-operation model contains:

    TRAIN
     ├── TRAIN_TYPE
     │
     ├── TRAIN_ROUTE
     │      └── TRAIN_ROUTE_STOPS
     │
     └── TRAIN_SCHEDULE
            └── TRAIN_RUN
                  ├── TRAIN_RUN_STOPS
                  ├── TRAIN_RUN_TRACK_SECTIONS
                  └── TRAIN_RUN_BLOCK_SECTIONS

Stations, track sections, and block sections provide the infrastructure context for train movement.

Exact relationships are defined by:

    docs/database-schema.mmd

---

# 13. Forecasting

Forecasting consists of:

    TRAFFIC_FORECASTS
            │
            ▼
    TRAFFIC_FORECAST_ITEMS

Forecast information is associated with railway divisions and forecast items are associated with relevant block sections and time windows.

Forecasting provides additional information that can influence scheduling.

Forecasting data is part of the operational backend domain and should not automatically be treated as a separate AI/ML database.

---

# 14. Scheduling Architecture

Scheduling is the core planning domain.

The finalized scheduling entities include:

    SCHEDULING_RUNS
           │
           ├── SCHEDULING_RUN_TASKS
           │
           ├── SCHEDULED_TASKS
           │
           └── SCHEDULED_BLOCKS
                  │
                  └── SCHEDULED_BLOCK_RESOURCES

Conceptually:

    Scheduling Run
          │
          ├── Planning Tasks
          │
          ├── Scheduled Maintenance Tasks
          │
          └── Scheduled Blocks
                   │
                   └── Allocated Resources

A scheduling run represents one execution of the planning process.

The resulting scheduled blocks represent the coordinated maintenance plan.

---

# 15. Conflict Architecture

Conflicts are represented by:

    SCHEDULE_CONFLICTS

A conflict can be associated with scheduling and affected operational entities according to the finalized schema.

Conceptually:

    Schedule
       │
       ├── Train conflict
       ├── Resource conflict
       ├── Track conflict
       ├── Block conflict
       └── Other scheduling conflict

The exact conflict types and relationships are determined by the database/API specifications.

---

# 16. Metrics Architecture

Scheduling results are represented by:

    SCHEDULE_METRICS
    SCHEDULE_BASELINE_METRICS

Conceptually:

    Scheduling Run
          │
          ├──────────────► Schedule Metrics
          │
          └──────────────► Baseline Metrics

Metrics provide a way to evaluate scheduling outcomes and compare them with baseline conditions.

The system should calculate metrics from actual scheduling data rather than hard-coding presentation/demo values.

---

# 17. Persistence of AI/Optimization Results

AI/optimization results are not required to have a generic AI results table.

The final operational result can be represented using existing scheduling-related tables:

    SCHEDULED_TASKS
    SCHEDULED_BLOCKS
    SCHEDULED_BLOCK_RESOURCES
    SCHEDULE_CONFLICTS
    SCHEDULE_METRICS

The general flow is:

    Python
      │
      │ Optimization result
      ▼
    Spring Boot
      │
      ├── Validate
      ├── Transform
      └── Persist
      │
      ▼
    Existing scheduling tables

Temporary AI/ML computation data may remain in application memory or temporary processing structures when appropriate.

Persistent AI model history, model versions, or recommendations should only be introduced through an explicit future database design decision.

---

# 18. API Architecture

The REST API is defined in:

    docs/api.yaml

The API is organized into:

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

The frontend communicates with these APIs through Spring Boot.

The API specification defines the external contract.

The internal implementation may contain additional classes or internal methods that are not exposed as APIs.

---

# 19. Module Interaction

A simplified domain interaction is:

    Organization
         │
         ▼
    Infrastructure ───────► Assets
         │                    │
         │                    ▼
         │                Maintenance
         │                    │
         ▼                    │
    Train Operations          │
         │                    │
         └─────────┬──────────┘
                   ▼
              Forecasting
                   │
                   ▼
              Scheduling
                   │
              ┌────┴────┐
              ▼         ▼
          Conflicts   Metrics
                   │
                   ▼
              Final Result

AI/ML supports the scheduling process:

    Operational Data
           │
           ▼
       Spring Boot
           │
           ▼
       Python AI/ML
           │
           ▼
    Scheduling Result
           │
           ▼
       Spring Boot

---

# 20. Frontend / Backend Boundary

The frontend is responsible for presentation and interaction.

The backend is responsible for business decisions and persistent state.

The frontend should not implement authoritative scheduling logic.

For example:

    Frontend:
    "Generate optimized plan"
            │
            ▼
    Backend:
    "Run scheduling/optimization"
            │
            ▼
    AI/ML:
    "Calculate optimization"
            │
            ▼
    Backend:
    "Validate and persist result"
            │
            ▼
    Frontend:
    "Display resulting plan"

---

# 21. Backend / AI Boundary

Spring Boot owns the application workflow.

Python owns AI/ML computation.

### Spring Boot

    Authentication
    Business workflow
    Data retrieval
    Data validation
    AI service orchestration
    Persistence
    API responses

### Python

    Prediction
    Optimization
    Model inference
    AI/ML calculations

Python should return structured results to Spring Boot.

It should not become a second application backend.

---

# 22. Prototype Boundary

A separate prototype PRD exists for demonstrating the concept.

The prototype may use:

- simplified models
- mocked data
- simulated optimization
- simulated live operations
- prototype-specific screens
- demo-mode workflows

The prototype is useful for understanding product intent and user workflows.

It is not the architectural source of truth for this backend.

The main backend architecture is based on:

    Spring Boot
    PostgreSQL
    Firebase Authentication
    Python AI/ML service
    REST APIs

and the finalized database/API specifications.

---

# 23. Development Reference Files

The backend repository should contain:

    Backend/
    │
    ├── AGENTS.md
    ├── ARCHITECTURE.md
    │
    ├── .cursor/
    │   └── rules/
    │       ├── spring-boot.mdc
    │       ├── database.mdc
    │       └── api.mdc
    │
    ├── docs/
    │   ├── database-schema.mmd
    │   └── api.yaml
    │
    ├── pom.xml
    │
    └── src/

### AGENTS.md

Defines how an AI coding agent should work on the project.

### ARCHITECTURE.md

Defines the system architecture and component responsibilities.

### .cursor/rules/

Contains implementation-specific rules used by the AI coding environment.

### spring-boot.mdc

Defines Spring Boot, Java, layered architecture, JPA, controller, service, repository, security, and application implementation rules.

### database.mdc

Defines database, PostgreSQL, JPA mapping, and Flyway migration rules.

### api.mdc

Defines REST API implementation rules and OpenAPI contract requirements.

### database-schema.mmd

Defines the authoritative PostgreSQL schema.

### api.yaml

Defines the authoritative REST API contract.

---

# 24. Architecture Principles

The system follows these principles:

### Separation of concerns

    Frontend → Presentation
    Spring Boot → Application / Business Logic
    Python → AI/ML
    PostgreSQL → Persistence
    Flyway → Database Schema Management

### Schema-first development

The database schema is established before backend implementation.

The finalized database schema is the source of truth for database structure.

### Version-controlled database evolution

Database schema changes are managed through Flyway migrations.

Hibernate/JPA does not create or modify the database schema.

### API-contract-driven development

Backend endpoints implement the finalized OpenAPI contract.

### Centralized backend orchestration

Spring Boot coordinates the application workflow.

### AI service isolation

AI/ML processing is isolated in Python.

### Persistent operational state

Spring Boot owns persistence of final application results.

### Incremental implementation

Modules are implemented and verified individually rather than generating the entire backend at once.

### Separation of database and API models

Database entities and API DTOs are separate concerns.

The API should expose only the fields required by the documented business/API contract.

---

# 25. High-Level End-to-End Flow

The complete intended workflow is:

                         USER
                           │
                           ▼
                     React Frontend
                           │
                           ▼
                      Firebase Auth
                           │
                           ▼
                     Spring Boot API
                           │
             ┌─────────────┼──────────────┐
             │             │              │
             ▼             ▼              ▼
      Infrastructure   Train Ops      Maintenance
             │             │              │
             └─────────────┼──────────────┘
                           │
                           ▼
                      Forecasting
                           │
                           ▼
                       Scheduling
                           │
                           ▼
                   Python AI/ML Service
                           │
                           ▼
                   Optimized Schedule
                           │
                  ┌────────┴────────┐
                  ▼                 ▼
              Conflicts          Metrics
                  │                 │
                  └────────┬────────┘
                           ▼
                       PostgreSQL
                           │
                           ▼
                     Spring Boot
                           │
                           ▼
                     React Dashboard

This architecture provides the foundation for the RailOpt-AI backend while keeping the frontend, backend, database, database migrations, and AI/ML responsibilities clearly separated.