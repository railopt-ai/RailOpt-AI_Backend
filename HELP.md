# RailOpt-AI Backend — Help

## Project

RailOpt-AI is the Spring Boot backend for:

**SIH26027 — AI-Powered Automatic Block Planning to Maximize Asset Availability for Train Operations on Indian Railways**

The backend provides REST APIs, business logic, database access, authentication, scheduling, conflict management, metrics, and integration with the Python AI/ML service.

---

## Technology Stack

- Java
- Spring Boot
- Maven
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Firebase Authentication
- Python AI/ML Service
- REST APIs

---

## Project Structure

```text
Backend/
├── .cursor/
│   └── rules/
├── docs/
│   ├── api.yaml
│   └── database-schema.mmd
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       └── db/
│   │           └── migration/
│   └── test/
├── AGENTS.md
├── ARCHITECTURE.md
├── HELP.md
├── README.md
├── pom.xml
├── mvnw
└── mvnw.cmd
```

---

## Important Documentation

Before implementing backend functionality, read:

- **`AGENTS.md`**  
  Defines the overall rules for AI-assisted development and project conventions.

- **`ARCHITECTURE.md`**  
  Defines the system architecture, application boundaries, responsibilities, and data flow.

- **`docs/database-schema.mmd`**  
  Authoritative database schema. Always check this before implementing database-related functionality.

- **`docs/api.yaml`**  
  Authoritative REST API contract. Always check this before implementing or modifying an API.

- **`.cursor/rules/`**  
  Contains implementation-specific rules for:
  - Spring Boot
  - Database / JPA / Flyway
  - REST API / OpenAPI

---

## Development Rule

For every new module, follow:

```text
Database Schema
      ↓
API Specification
      ↓
Architecture
      ↓
Existing Code
      ↓
Implementation
      ↓
Validation
      ↓
Testing
```

Do not develop APIs independently of the finalized database schema.

---

## Running the Application

### Using Maven Wrapper

**Linux/macOS:**
```bash
./mvnw spring-boot:run
```

**Windows:**
```cmd
.\mvnw.cmd spring-boot:run
```

### Building the Project

**Linux/macOS:**
```bash
./mvnw clean package
```

**Windows:**
```cmd
.\mvnw.cmd clean package
```

### Running Tests

**Linux/macOS:**
```bash
./mvnw test
```

**Windows:**
```cmd
.\mvnw.cmd test
```

---

## Database

The application uses PostgreSQL.

Database schema management is handled by Flyway.

Hibernate/JPA validates the schema but does not create or modify it.

Expected configuration:

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

Flyway migrations are stored in:

`src/main/resources/db/migration/`

Example:
- `V1__initial_schema.sql`
- `V2__add_forecasting_tables.sql`
- `V3__add_scheduling_tables.sql`

Never use:
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.jpa.hibernate.ddl-auto=create`
- `spring.jpa.hibernate.ddl-auto=create-drop`

for normal project development.

---

## Authentication

Authentication is handled using Firebase Authentication.

The frontend sends the Firebase ID token using:

```http
Authorization: Bearer <firebase-id-token>
```

Spring Boot verifies the token before processing protected requests.

Never commit Firebase credentials or service-account files.

---

## AI/ML Service

The Python AI/ML service is separate from the Spring Boot backend.

The normal flow is:

```text
React
  ↓
Spring Boot
  ↓
Python AI/ML Service
  ↓
Spring Boot
  ↓
PostgreSQL
```

The Python service must not directly access PostgreSQL.

Spring Boot remains responsible for application persistence and orchestration.

---

## API Documentation

The REST API specification is available at:

`docs/api.yaml`

When changing an API:
1. Update the API specification.
2. Update the DTOs if required.
3. Update the controller.
4. Update the service.
5. Update tests.
6. Verify the implementation against `docs/api.yaml`.

---

## Database Changes

When changing the database:
1. Update `docs/database-schema.mmd`.
2. Create a new Flyway migration.
3. Update JPA entities.
4. Update repositories if required.
5. Update affected services/APIs.
6. Validate the application.
7. Run tests.

Do not modify an already-applied Flyway migration.

---

## Git

Before committing:

```bash
git status
```

Review staged files before committing.

Typical workflow:

```bash
git add .
git commit -m "Description of change"
git push
```

Do not commit:
- passwords
- API keys
- Firebase credentials
- service-account files
- `.env` files
- build output
- IDE-specific files

---

## Development Principle

Keep the architecture simple and consistent.

- **Spring Boot** is the central backend and application orchestrator.
- **PostgreSQL** is the persistent data store.
- **Flyway** manages database schema evolution.
- **Python** handles AI/ML-specific computation.
- **React** communicates with Spring Boot through REST APIs.

Follow the documented project architecture instead of introducing unnecessary technologies or abstractions.
