# Database Implementation Reference

This document records what was **actually implemented** in:

`src/main/resources/db/migration/V1__INITIAL_DATABASE_CREATION.sql`

It is **not** a replacement for `docs/database-schema.mmd`.

- `docs/database-schema.mmd` remains the single source of truth for the **intended** database design.
- This file records the **actual PostgreSQL** types, nullability, and constraints used in V1.
- Future JPA entities must match this PostgreSQL schema.

Derived from the V1 SQL after a column-by-column review against `docs/database-schema.mmd`.

---

## 1. Scope of V1

| Item | Implementation |
|---|---|
| Tables created | 46 |
| PostgreSQL ENUM types | None |
| CHECK constraints | None |
| Extra (non-PK/UK) indexes | None |
| `ON DELETE` / `ON UPDATE` actions | Not specified in schema; PostgreSQL default `NO ACTION` |
| UUID generation defaults | Not specified; no `DEFAULT gen_random_uuid()` |
| Identifier quoting | Unquoted; PostgreSQL stores lowercase names |

Physical table names are lowercase (`users`, not `USERS`). Column names match the schema in snake_case.

---

## 2. PostgreSQL type mapping

| Schema type | PostgreSQL type | JPA / Java mapping note |
|---|---|---|
| UUID | `UUID` | `java.util.UUID` |
| VARCHAR | `VARCHAR` with **no length** | `String`; no `@Column(length=…)` business maximum |
| TEXT | `TEXT` | `String`; `@Column(columnDefinition = "TEXT")` if needed |
| DECIMAL | `NUMERIC` with **no precision/scale** | `java.math.BigDecimal` |
| INTEGER | `INTEGER` | `Integer` |
| BOOLEAN | `BOOLEAN` | `Boolean` (nullable) |
| DATE | `DATE` | `java.time.LocalDate` |
| TIMESTAMP | `TIMESTAMP` (without time zone) | `java.time.LocalDateTime` |
| TIME | `TIME` (without time zone) | `java.time.LocalTime` |
| JSONB | `JSONB` | Hibernate JSON type / `JsonNode` / `Map`; not a plain `String` without mapping |

`VARCHAR` without a length is valid PostgreSQL and is **not** the same as inventing `VARCHAR(255)`. Maximum string length: **Not specified** in the schema for every VARCHAR field.

---

## 3. Enums

`docs/database-schema.mmd` does **not** define a finite set of allowed values for status, type, category, severity, or similar fields.

V1 therefore:

- does **not** create PostgreSQL `ENUM` types
- stores those fields as unbounded `VARCHAR`
- does **not** add `CHECK` lists of values

Allowed values: **Not specified**.

When Java enums are introduced later, they must be an explicit design decision synchronized with both the schema and this VARCHAR representation. Do not assume PostgreSQL enum types exist.

Fields that look enum-like but are implemented as `VARCHAR`:

- `status` (many tables)
- `station_type`, `track_type`, `gauge`, `electrification_status`
- `block_system`, `availability_type`
- `criticality`, `condition_status`
- `defect_type`, `severity`
- `maintenance_category`, `priority`, `resource_type`, `requirement_type`
- `category`, `priority_class`, `operator`
- `operating_days` (stored as VARCHAR; format **Not specified**)
- `forecast_type`, `train_category`
- `algorithm_type`, `consideration_status`, `unscheduled_reason`
- `conflict_type`, `prediction_type`

---

## 4. Nullability rules used in V1

The mermaid schema does not mark columns as NULL / NOT NULL except through PK / UK / relationship notation.

| Rule | Applied in V1 |
|---|---|
| Primary-key columns | `NOT NULL` |
| Unique-key (`UK`) columns | `NOT NULL UNIQUE` (implementation: UK treated as a required identifier) |
| Composite-PK FK columns | `NOT NULL` |
| Identifying parent FKs (`\|\|--o{` / `\|\|--o\|`) | `NOT NULL` except the exceptions below |
| All other columns | `NULL` (nullable) — requiredness **Not specified** |

Nullable foreign keys (exceptions):

| Table | Column | Reason recorded at implementation |
|---|---|---|
| `role_enrollments` | `reviewed_by` | Review may not have occurred yet |
| `assets` | `track_section_id` | Asset may be located at a station only |
| `assets` | `station_id` | Asset may be located on a track section only |
| `maintenance_tasks` | `defect_id` | Schema relationship is “may generate” |
| `schedule_conflicts` | `scheduled_task_id` | Conflict may involve a subset of related entities |
| `schedule_conflicts` | `scheduled_block_id` | same |
| `schedule_conflicts` | `train_run_id` | same |
| `schedule_conflicts` | `resource_id` | same |
| `schedule_conflicts` | `track_section_id` | same |
| `schedule_conflicts` | `block_section_id` | same |

The `\|\|--o\|` metrics relationships (`schedule_metrics`, `schedule_baseline_metrics`) are **not** implemented as `UNIQUE(scheduling_run_id)`. Unique constraints are only those marked `UK` in the schema.

---

## 5. Constraints that were **not** added

Not specified in the schema, therefore **not** implemented:

- CHECK constraints (ranges, percentages 0–100, `available_until > available_from`, mutually exclusive locations, etc.)
- Foreign-key `ON DELETE CASCADE` / `SET NULL` / `RESTRICT` overrides
- Application performance indexes on FK columns
- Unique constraint for one-metrics-row-per-scheduling-run
- Default timestamps or UUID generation
- Audit fields beyond those listed in the schema (`deleted_at` is not present)

---

## 6. Tables

### `users`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `google_subject_id` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `email` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `profile_picture_url` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `last_login_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `google_subject_id`, `email`
- **Foreign keys:** none
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `roles`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:** none
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `user_roles`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `user_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `role_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `assigned_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key (composite):** (`user_id`, `role_id`)
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `user_id` → `users.id` (NOT NULL, `NO ACTION`)
  - `role_id` → `roles.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `departments`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:** none
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `role_enrollments`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `user_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `role_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `request_reason` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `requested_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `reviewed_by` | `UUID` | NULL | n/a | NULL allowed |
| `reviewed_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `review_reason` | `TEXT` | NULL | unlimited TEXT | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `user_id` → `users.id` (NOT NULL, `NO ACTION`)
  - `role_id` → `roles.id` (NOT NULL, `NO ACTION`)
  - `reviewed_by` → `users.id` (NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `user_departments`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `user_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `department_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `joined_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `left_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key (composite):** (`user_id`, `department_id`)
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `user_id` → `users.id` (NOT NULL, `NO ACTION`)
  - `department_id` → `departments.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `railways`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:** none
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `railway_zones`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `railway_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:**
  - `railway_id` → `railways.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `railway_divisions`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `zone_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:**
  - `zone_id` → `railway_zones.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `stations`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `division_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `latitude` | `NUMERIC` | NULL | n/a | NULL allowed |
| `longitude` | `NUMERIC` | NULL | n/a | NULL allowed |
| `station_type` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:**
  - `division_id` → `railway_divisions.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `tracks`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `division_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `track_type` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `gauge` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `electrification_status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:**
  - `division_id` → `railway_divisions.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `track_sections`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `track_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `from_station_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `to_station_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `length_km` | `NUMERIC` | NULL | n/a | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:**
  - `track_id` → `tracks.id` (NOT NULL, `NO ACTION`)
  - `from_station_id` → `stations.id` (NOT NULL, `NO ACTION`)
  - `to_station_id` → `stations.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `block_sections`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `division_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `from_station_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `to_station_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `block_system` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:**
  - `division_id` → `railway_divisions.id` (NOT NULL, `NO ACTION`)
  - `from_station_id` → `stations.id` (NOT NULL, `NO ACTION`)
  - `to_station_id` → `stations.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `block_availability_windows`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `block_section_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `available_from` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `available_until` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `availability_type` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `restriction_reason` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `block_section_id` → `block_sections.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `corridors`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `division_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:**
  - `division_id` → `railway_divisions.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `corridor_track_sections`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `corridor_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `track_section_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `sequence_no` | `INTEGER` | NULL | n/a | NULL allowed |

- **Primary key (composite):** (`corridor_id`, `track_section_id`)
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `corridor_id` → `corridors.id` (NOT NULL, `NO ACTION`)
  - `track_section_id` → `track_sections.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `corridor_block_sections`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `corridor_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `block_section_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `sequence_no` | `INTEGER` | NULL | n/a | NULL allowed |

- **Primary key (composite):** (`corridor_id`, `block_section_id`)
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `corridor_id` → `corridors.id` (NOT NULL, `NO ACTION`)
  - `block_section_id` → `block_sections.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `asset_types`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `department_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:**
  - `department_id` → `departments.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `assets`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `asset_type_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `track_section_id` | `UUID` | NULL | n/a | NULL allowed |
| `station_id` | `UUID` | NULL | n/a | NULL allowed |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `location_km` | `NUMERIC` | NULL | n/a | NULL allowed |
| `installation_date` | `DATE` | NULL | n/a | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `criticality` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `condition_status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `availability_percentage` | `NUMERIC` | NULL | n/a | NULL allowed |
| `last_maintenance_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `next_maintenance_due_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:**
  - `asset_type_id` → `asset_types.id` (NOT NULL, `NO ACTION`)
  - `track_section_id` → `track_sections.id` (NULL, `NO ACTION`)
  - `station_id` → `stations.id` (NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `defects`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `asset_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `reported_by` | `UUID` | NOT NULL | n/a | NOT NULL |
| `defect_code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `defect_type` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `severity` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `detected_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `due_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `resolved_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `defect_code`
- **Foreign keys:**
  - `asset_id` → `assets.id` (NOT NULL, `NO ACTION`)
  - `reported_by` → `users.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `maintenance_types`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `department_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `maintenance_category` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:**
  - `department_id` → `departments.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `maintenance_tasks`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `asset_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `defect_id` | `UUID` | NULL | n/a | NULL allowed |
| `maintenance_type_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `assigned_department_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `assigned_by` | `UUID` | NOT NULL | n/a | NOT NULL |
| `task_code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `priority` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `estimated_duration_hours` | `NUMERIC` | NULL | n/a | NULL allowed |
| `actual_duration_hours` | `NUMERIC` | NULL | n/a | NULL allowed |
| `requested_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `scheduled_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `completed_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `deadline_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `task_code`
- **Foreign keys:**
  - `asset_id` → `assets.id` (NOT NULL, `NO ACTION`)
  - `defect_id` → `defects.id` (NULL, `NO ACTION`)
  - `maintenance_type_id` → `maintenance_types.id` (NOT NULL, `NO ACTION`)
  - `assigned_department_id` → `departments.id` (NOT NULL, `NO ACTION`)
  - `assigned_by` → `users.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `maintenance_resources`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `department_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `resource_type` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `capacity` | `INTEGER` | NULL | n/a | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:**
  - `department_id` → `departments.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `maintenance_task_resources`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `maintenance_task_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `resource_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `required_quantity` | `INTEGER` | NULL | n/a | NULL allowed |

- **Primary key (composite):** (`maintenance_task_id`, `resource_id`)
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `maintenance_task_id` → `maintenance_tasks.id` (NOT NULL, `NO ACTION`)
  - `resource_id` → `maintenance_resources.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `resource_availability`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `resource_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `available_from` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `available_until` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `available_quantity` | `INTEGER` | NULL | n/a | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `resource_id` → `maintenance_resources.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `maintenance_task_block_requirements`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `maintenance_task_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `block_section_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `requirement_type` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `requires_full_block` | `BOOLEAN` | NULL | n/a | NULL allowed |
| `requires_power_block` | `BOOLEAN` | NULL | n/a | NULL allowed |
| `requires_signal_block` | `BOOLEAN` | NULL | n/a | NULL allowed |
| `requires_speed_restriction` | `BOOLEAN` | NULL | n/a | NULL allowed |
| `safety_buffer_minutes` | `INTEGER` | NULL | n/a | NULL allowed |
| `minimum_duration_hours` | `NUMERIC` | NULL | n/a | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key (composite):** (`maintenance_task_id`, `block_section_id`)
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `maintenance_task_id` → `maintenance_tasks.id` (NOT NULL, `NO ACTION`)
  - `block_section_id` → `block_sections.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `train_types`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `category` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `priority_class` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `code`
- **Foreign keys:** none
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `trains`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `train_type_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `train_number` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `train_name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `operator` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `train_number`
- **Foreign keys:**
  - `train_type_id` → `train_types.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `train_routes`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `train_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `route_code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `origin_station_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `destination_station_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `total_stations` | `INTEGER` | NULL | n/a | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `route_code`
- **Foreign keys:**
  - `train_id` → `trains.id` (NOT NULL, `NO ACTION`)
  - `origin_station_id` → `stations.id` (NOT NULL, `NO ACTION`)
  - `destination_station_id` → `stations.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `train_route_stops`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `train_route_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `station_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `sequence_no` | `INTEGER` | NULL | n/a | NULL allowed |
| `arrival_offset_minutes` | `INTEGER` | NULL | n/a | NULL allowed |
| `departure_offset_minutes` | `INTEGER` | NULL | n/a | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `train_route_id` → `train_routes.id` (NOT NULL, `NO ACTION`)
  - `station_id` → `stations.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `train_schedules`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `train_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `train_route_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `schedule_code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `valid_from` | `DATE` | NULL | n/a | NULL allowed |
| `valid_until` | `DATE` | NULL | n/a | NULL allowed |
| `operating_days` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `departure_time` | `TIME` | NULL | n/a | NULL allowed |
| `arrival_time` | `TIME` | NULL | n/a | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `schedule_code`
- **Foreign keys:**
  - `train_id` → `trains.id` (NOT NULL, `NO ACTION`)
  - `train_route_id` → `train_routes.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `train_runs`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `train_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `train_route_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `train_schedule_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `run_code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `operation_date` | `DATE` | NULL | n/a | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `planned_start_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `planned_end_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `actual_start_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `actual_end_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `run_code`
- **Foreign keys:**
  - `train_id` → `trains.id` (NOT NULL, `NO ACTION`)
  - `train_route_id` → `train_routes.id` (NOT NULL, `NO ACTION`)
  - `train_schedule_id` → `train_schedules.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `train_run_stops`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `train_run_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `station_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `sequence_no` | `INTEGER` | NULL | n/a | NULL allowed |
| `planned_arrival_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `planned_departure_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `actual_arrival_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `actual_departure_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `train_run_id` → `train_runs.id` (NOT NULL, `NO ACTION`)
  - `station_id` → `stations.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `train_run_track_sections`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `train_run_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `track_section_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `sequence_no` | `INTEGER` | NULL | n/a | NULL allowed |
| `planned_entry_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `planned_exit_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key (composite):** (`train_run_id`, `track_section_id`)
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `train_run_id` → `train_runs.id` (NOT NULL, `NO ACTION`)
  - `track_section_id` → `track_sections.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `train_run_block_sections`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `train_run_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `block_section_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `sequence_no` | `INTEGER` | NULL | n/a | NULL allowed |
| `planned_entry_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `planned_exit_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key (composite):** (`train_run_id`, `block_section_id`)
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `train_run_id` → `train_runs.id` (NOT NULL, `NO ACTION`)
  - `block_section_id` → `block_sections.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `traffic_forecasts`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `division_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `forecast_type` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `forecast_date` | `DATE` | NULL | n/a | NULL allowed |
| `horizon_start_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `horizon_end_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `division_id` → `railway_divisions.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `traffic_forecast_items`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `forecast_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `block_section_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `time_window_start` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `time_window_end` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `train_category` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `expected_train_count` | `INTEGER` | NULL | n/a | NULL allowed |
| `probability` | `NUMERIC` | NULL | n/a | NULL allowed |
| `expected_passenger_trains` | `INTEGER` | NULL | n/a | NULL allowed |
| `expected_goods_trains` | `INTEGER` | NULL | n/a | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `forecast_id` → `traffic_forecasts.id` (NOT NULL, `NO ACTION`)
  - `block_section_id` → `block_sections.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `scheduling_runs`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `created_by` | `UUID` | NOT NULL | n/a | NOT NULL |
| `run_code` | `VARCHAR` | NOT NULL | Not specified (unbounded VARCHAR) | UNIQUE, NOT NULL |
| `algorithm_type` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `planning_start_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `planning_end_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `started_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `completed_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `objective_description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `objective_weights` | `JSONB` | NULL | n/a | NULL allowed |
| `total_tasks_considered` | `INTEGER` | NULL | n/a | NULL allowed |
| `total_tasks_scheduled` | `INTEGER` | NULL | n/a | NULL allowed |
| `total_tasks_unscheduled` | `INTEGER` | NULL | n/a | NULL allowed |
| `error_message` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** `run_code`
- **Foreign keys:**
  - `created_by` → `users.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index; unique index on UK column(s). No additional indexes.

### `scheduling_run_tasks`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `scheduling_run_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `maintenance_task_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `consideration_status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `unscheduled_reason` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `predicted_duration_hours` | `NUMERIC` | NULL | n/a | NULL allowed |
| `effective_duration_hours` | `NUMERIC` | NULL | n/a | NULL allowed |
| `priority_score` | `NUMERIC` | NULL | n/a | NULL allowed |
| `risk_score` | `NUMERIC` | NULL | n/a | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key (composite):** (`scheduling_run_id`, `maintenance_task_id`)
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `scheduling_run_id` → `scheduling_runs.id` (NOT NULL, `NO ACTION`)
  - `maintenance_task_id` → `maintenance_tasks.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `scheduled_tasks`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `scheduling_run_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `maintenance_task_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `planned_start_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `planned_end_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `planned_duration_hours` | `NUMERIC` | NULL | n/a | NULL allowed |
| `objective_contribution` | `NUMERIC` | NULL | n/a | NULL allowed |
| `scheduling_reason` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `scheduling_run_id` → `scheduling_runs.id` (NOT NULL, `NO ACTION`)
  - `maintenance_task_id` → `maintenance_tasks.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `scheduled_blocks`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `scheduling_run_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `scheduled_task_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `block_section_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `block_code` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `planned_start_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `planned_end_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `duration_minutes` | `INTEGER` | NULL | n/a | NULL allowed |
| `full_block` | `BOOLEAN` | NULL | n/a | NULL allowed |
| `power_disconnection` | `BOOLEAN` | NULL | n/a | NULL allowed |
| `train_impact_score` | `NUMERIC` | NULL | n/a | NULL allowed |
| `asset_impact_score` | `NUMERIC` | NULL | n/a | NULL allowed |
| `scheduling_reason` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `updated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `scheduling_run_id` → `scheduling_runs.id` (NOT NULL, `NO ACTION`)
  - `scheduled_task_id` → `scheduled_tasks.id` (NOT NULL, `NO ACTION`)
  - `block_section_id` → `block_sections.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `scheduled_block_resources`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `scheduled_block_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `resource_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `quantity_allocated` | `INTEGER` | NULL | n/a | NULL allowed |
| `allocated_start_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `allocated_end_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key (composite):** (`scheduled_block_id`, `resource_id`)
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `scheduled_block_id` → `scheduled_blocks.id` (NOT NULL, `NO ACTION`)
  - `resource_id` → `maintenance_resources.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `schedule_conflicts`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `scheduling_run_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `scheduled_task_id` | `UUID` | NULL | n/a | NULL allowed |
| `scheduled_block_id` | `UUID` | NULL | n/a | NULL allowed |
| `train_run_id` | `UUID` | NULL | n/a | NULL allowed |
| `resource_id` | `UUID` | NULL | n/a | NULL allowed |
| `track_section_id` | `UUID` | NULL | n/a | NULL allowed |
| `block_section_id` | `UUID` | NULL | n/a | NULL allowed |
| `conflict_type` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `severity` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `status` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `description` | `TEXT` | NULL | unlimited TEXT | NULL allowed |
| `conflict_duration_minutes` | `INTEGER` | NULL | n/a | NULL allowed |
| `conflict_start_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `conflict_end_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `created_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |
| `resolved_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `scheduling_run_id` → `scheduling_runs.id` (NOT NULL, `NO ACTION`)
  - `scheduled_task_id` → `scheduled_tasks.id` (NULL, `NO ACTION`)
  - `scheduled_block_id` → `scheduled_blocks.id` (NULL, `NO ACTION`)
  - `train_run_id` → `train_runs.id` (NULL, `NO ACTION`)
  - `resource_id` → `maintenance_resources.id` (NULL, `NO ACTION`)
  - `track_section_id` → `track_sections.id` (NULL, `NO ACTION`)
  - `block_section_id` → `block_sections.id` (NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `schedule_metrics`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `scheduling_run_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `total_tasks` | `INTEGER` | NULL | n/a | NULL allowed |
| `scheduled_tasks` | `INTEGER` | NULL | n/a | NULL allowed |
| `unscheduled_tasks` | `INTEGER` | NULL | n/a | NULL allowed |
| `total_blocks` | `INTEGER` | NULL | n/a | NULL allowed |
| `total_block_hours` | `INTEGER` | NULL | n/a | NULL allowed |
| `train_conflicts` | `INTEGER` | NULL | n/a | NULL allowed |
| `resource_conflicts` | `INTEGER` | NULL | n/a | NULL allowed |
| `safety_conflicts` | `INTEGER` | NULL | n/a | NULL allowed |
| `estimated_train_delay_minutes` | `NUMERIC` | NULL | n/a | NULL allowed |
| `estimated_asset_downtime_hours` | `NUMERIC` | NULL | n/a | NULL allowed |
| `estimated_asset_availability_percentage` | `NUMERIC` | NULL | n/a | NULL allowed |
| `resource_utilization_percentage` | `NUMERIC` | NULL | n/a | NULL allowed |
| `maintenance_completion_percentage` | `NUMERIC` | NULL | n/a | NULL allowed |
| `total_objective_score` | `NUMERIC` | NULL | n/a | NULL allowed |
| `calculated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `scheduling_run_id` → `scheduling_runs.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `schedule_baseline_metrics`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `scheduling_run_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `total_tasks` | `INTEGER` | NULL | n/a | NULL allowed |
| `tasks_without_optimized_scheduling` | `INTEGER` | NULL | n/a | NULL allowed |
| `estimated_block_hours` | `INTEGER` | NULL | n/a | NULL allowed |
| `estimated_train_delay_minutes` | `NUMERIC` | NULL | n/a | NULL allowed |
| `estimated_asset_downtime_hours` | `NUMERIC` | NULL | n/a | NULL allowed |
| `estimated_asset_availability_percentage` | `NUMERIC` | NULL | n/a | NULL allowed |
| `resource_utilization_percentage` | `NUMERIC` | NULL | n/a | NULL allowed |
| `calculated_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `scheduling_run_id` → `scheduling_runs.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

### `scheduling_predictions`

| Column | PostgreSQL type | Nullability | Max length | Constraints |
|---|---|---|---|---|
| `id` | `UUID` | NOT NULL | n/a | PRIMARY KEY, NOT NULL |
| `scheduling_run_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `maintenance_task_id` | `UUID` | NOT NULL | n/a | NOT NULL |
| `prediction_type` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `predicted_value` | `NUMERIC` | NULL | n/a | NULL allowed |
| `model_name` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `model_version` | `VARCHAR` | NULL | Not specified (unbounded VARCHAR) | NULL allowed |
| `confidence_score` | `NUMERIC` | NULL | n/a | NULL allowed |
| `input_features` | `JSONB` | NULL | n/a | NULL allowed |
| `predicted_at` | `TIMESTAMP` | NULL | n/a | NULL allowed |

- **Primary key:** `id`
- **Unique:** none beyond the primary key
- **Foreign keys:**
  - `scheduling_run_id` → `scheduling_runs.id` (NOT NULL, `NO ACTION`)
  - `maintenance_task_id` → `maintenance_tasks.id` (NOT NULL, `NO ACTION`)
- **CHECK:** none
- **Indexes:** primary key index. No additional indexes.

---

## 7. Relationship summary (implemented FKs)

| Child table | Column | Parent table | Parent column | Nullability |
|---|---|---|---|---|
| `user_roles` | `user_id` | `users` | `id` | NOT NULL |
| `user_roles` | `role_id` | `roles` | `id` | NOT NULL |
| `role_enrollments` | `user_id` | `users` | `id` | NOT NULL |
| `role_enrollments` | `role_id` | `roles` | `id` | NOT NULL |
| `role_enrollments` | `reviewed_by` | `users` | `id` | NULL |
| `user_departments` | `user_id` | `users` | `id` | NOT NULL |
| `user_departments` | `department_id` | `departments` | `id` | NOT NULL |
| `railway_zones` | `railway_id` | `railways` | `id` | NOT NULL |
| `railway_divisions` | `zone_id` | `railway_zones` | `id` | NOT NULL |
| `stations` | `division_id` | `railway_divisions` | `id` | NOT NULL |
| `tracks` | `division_id` | `railway_divisions` | `id` | NOT NULL |
| `track_sections` | `track_id` | `tracks` | `id` | NOT NULL |
| `track_sections` | `from_station_id` | `stations` | `id` | NOT NULL |
| `track_sections` | `to_station_id` | `stations` | `id` | NOT NULL |
| `block_sections` | `division_id` | `railway_divisions` | `id` | NOT NULL |
| `block_sections` | `from_station_id` | `stations` | `id` | NOT NULL |
| `block_sections` | `to_station_id` | `stations` | `id` | NOT NULL |
| `block_availability_windows` | `block_section_id` | `block_sections` | `id` | NOT NULL |
| `corridors` | `division_id` | `railway_divisions` | `id` | NOT NULL |
| `corridor_track_sections` | `corridor_id` | `corridors` | `id` | NOT NULL |
| `corridor_track_sections` | `track_section_id` | `track_sections` | `id` | NOT NULL |
| `corridor_block_sections` | `corridor_id` | `corridors` | `id` | NOT NULL |
| `corridor_block_sections` | `block_section_id` | `block_sections` | `id` | NOT NULL |
| `asset_types` | `department_id` | `departments` | `id` | NOT NULL |
| `assets` | `asset_type_id` | `asset_types` | `id` | NOT NULL |
| `assets` | `track_section_id` | `track_sections` | `id` | NULL |
| `assets` | `station_id` | `stations` | `id` | NULL |
| `defects` | `asset_id` | `assets` | `id` | NOT NULL |
| `defects` | `reported_by` | `users` | `id` | NOT NULL |
| `maintenance_types` | `department_id` | `departments` | `id` | NOT NULL |
| `maintenance_tasks` | `asset_id` | `assets` | `id` | NOT NULL |
| `maintenance_tasks` | `defect_id` | `defects` | `id` | NULL |
| `maintenance_tasks` | `maintenance_type_id` | `maintenance_types` | `id` | NOT NULL |
| `maintenance_tasks` | `assigned_department_id` | `departments` | `id` | NOT NULL |
| `maintenance_tasks` | `assigned_by` | `users` | `id` | NOT NULL |
| `maintenance_resources` | `department_id` | `departments` | `id` | NOT NULL |
| `maintenance_task_resources` | `maintenance_task_id` | `maintenance_tasks` | `id` | NOT NULL |
| `maintenance_task_resources` | `resource_id` | `maintenance_resources` | `id` | NOT NULL |
| `resource_availability` | `resource_id` | `maintenance_resources` | `id` | NOT NULL |
| `maintenance_task_block_requirements` | `maintenance_task_id` | `maintenance_tasks` | `id` | NOT NULL |
| `maintenance_task_block_requirements` | `block_section_id` | `block_sections` | `id` | NOT NULL |
| `trains` | `train_type_id` | `train_types` | `id` | NOT NULL |
| `train_routes` | `train_id` | `trains` | `id` | NOT NULL |
| `train_routes` | `origin_station_id` | `stations` | `id` | NOT NULL |
| `train_routes` | `destination_station_id` | `stations` | `id` | NOT NULL |
| `train_route_stops` | `train_route_id` | `train_routes` | `id` | NOT NULL |
| `train_route_stops` | `station_id` | `stations` | `id` | NOT NULL |
| `train_schedules` | `train_id` | `trains` | `id` | NOT NULL |
| `train_schedules` | `train_route_id` | `train_routes` | `id` | NOT NULL |
| `train_runs` | `train_id` | `trains` | `id` | NOT NULL |
| `train_runs` | `train_route_id` | `train_routes` | `id` | NOT NULL |
| `train_runs` | `train_schedule_id` | `train_schedules` | `id` | NOT NULL |
| `train_run_stops` | `train_run_id` | `train_runs` | `id` | NOT NULL |
| `train_run_stops` | `station_id` | `stations` | `id` | NOT NULL |
| `train_run_track_sections` | `train_run_id` | `train_runs` | `id` | NOT NULL |
| `train_run_track_sections` | `track_section_id` | `track_sections` | `id` | NOT NULL |
| `train_run_block_sections` | `train_run_id` | `train_runs` | `id` | NOT NULL |
| `train_run_block_sections` | `block_section_id` | `block_sections` | `id` | NOT NULL |
| `traffic_forecasts` | `division_id` | `railway_divisions` | `id` | NOT NULL |
| `traffic_forecast_items` | `forecast_id` | `traffic_forecasts` | `id` | NOT NULL |
| `traffic_forecast_items` | `block_section_id` | `block_sections` | `id` | NOT NULL |
| `scheduling_runs` | `created_by` | `users` | `id` | NOT NULL |
| `scheduling_run_tasks` | `scheduling_run_id` | `scheduling_runs` | `id` | NOT NULL |
| `scheduling_run_tasks` | `maintenance_task_id` | `maintenance_tasks` | `id` | NOT NULL |
| `scheduled_tasks` | `scheduling_run_id` | `scheduling_runs` | `id` | NOT NULL |
| `scheduled_tasks` | `maintenance_task_id` | `maintenance_tasks` | `id` | NOT NULL |
| `scheduled_blocks` | `scheduling_run_id` | `scheduling_runs` | `id` | NOT NULL |
| `scheduled_blocks` | `scheduled_task_id` | `scheduled_tasks` | `id` | NOT NULL |
| `scheduled_blocks` | `block_section_id` | `block_sections` | `id` | NOT NULL |
| `scheduled_block_resources` | `scheduled_block_id` | `scheduled_blocks` | `id` | NOT NULL |
| `scheduled_block_resources` | `resource_id` | `maintenance_resources` | `id` | NOT NULL |
| `schedule_conflicts` | `scheduling_run_id` | `scheduling_runs` | `id` | NOT NULL |
| `schedule_conflicts` | `scheduled_task_id` | `scheduled_tasks` | `id` | NULL |
| `schedule_conflicts` | `scheduled_block_id` | `scheduled_blocks` | `id` | NULL |
| `schedule_conflicts` | `train_run_id` | `train_runs` | `id` | NULL |
| `schedule_conflicts` | `resource_id` | `maintenance_resources` | `id` | NULL |
| `schedule_conflicts` | `track_section_id` | `track_sections` | `id` | NULL |
| `schedule_conflicts` | `block_section_id` | `block_sections` | `id` | NULL |
| `schedule_metrics` | `scheduling_run_id` | `scheduling_runs` | `id` | NOT NULL |
| `schedule_baseline_metrics` | `scheduling_run_id` | `scheduling_runs` | `id` | NOT NULL |
| `scheduling_predictions` | `scheduling_run_id` | `scheduling_runs` | `id` | NOT NULL |
| `scheduling_predictions` | `maintenance_task_id` | `maintenance_tasks` | `id` | NOT NULL |

---

## 8. JPA mapping notes

- Map `@Table(name = "users")` etc. using **lowercase** table names.
- Map `@Column(name = "google_subject_id")` using snake_case column names.
- Do not set `@Column(length = 255)` unless a later migration introduces a real maximum.
- `TIMESTAMP` → `LocalDateTime`; do not use `OffsetDateTime` / `Instant` unless the column is changed to `TIMESTAMPTZ`.
- `TIME` (`train_schedules.departure_time`, `arrival_time`) → `LocalTime`.
- `JSONB` columns (`scheduling_runs.objective_weights`, `scheduling_predictions.input_features`) are mapped as `Map<String, Object>` with `@JdbcTypeCode(SqlTypes.JSON)` and `columnDefinition = "jsonb"`. The JSON object shape is **Not specified**.
- Composite keys: `user_roles`, `user_departments`, `corridor_track_sections`, `corridor_block_sections`, `maintenance_task_resources`, `maintenance_task_block_requirements`, `train_run_track_sections`, `train_run_block_sections`, `scheduling_run_tasks`, `scheduled_block_resources`.
- Junction / association tables have no surrogate `id` unless the schema lists one (`train_route_stops` and `train_run_stops` **do** have `id`).
- Do not add entity fields for `created_at` / `updated_at` on tables that do not have those columns in V1 (for example `train_run_stops`, `scheduled_block_resources`, `scheduling_predictions`).
- Boolean columns are nullable; use `Boolean`, not primitive `boolean`, unless a later migration makes them `NOT NULL`.
- There are no PostgreSQL enums to map with `@Enumerated` against a native enum type; VARCHAR status fields may later use `@Enumerated(EnumType.STRING)` only if Java enum values are explicitly defined and kept in sync.

---

## 9. Details that could not be determined from the schema

| Topic | Status |
|---|---|
| VARCHAR maximum lengths | Not specified |
| NUMERIC precision and scale | Not specified |
| Allowed values for status/type/severity/category fields | Not specified |
| Time zone for TIMESTAMP | Not specified; implemented as timestamp without time zone |
| `operating_days` encoding | Not specified |
| UUID generation strategy | Not specified |
| Default values | Not specified |
| ON DELETE / ON UPDATE actions | Not specified |
| CHECK / domain rules (percentage bounds, date order, etc.) | Not specified |
| Extra indexes | Not specified |
| Uniqueness of `schedule_metrics.scheduling_run_id` / baseline equivalent | Cardinality `\|\|--o\|` is documented; unique constraint is **not** marked `UK` and was not added |
| Whether both `assets.track_section_id` and `assets.station_id` may be set together | Not specified; both nullable, no CHECK |
| Whether `schedule_conflicts` requires at least one of the optional FKs | Not specified; no CHECK |

---

## 10. Verification

Reviewed V1 SQL against `docs/database-schema.mmd`:

- Every documented table exists (46).
- Every documented column exists.
- No undocumented tables or columns.
- Table and column names match (PostgreSQL lowercase folding).
- Primary keys match, including composite keys.
- Unique constraints match documented `UK` columns only.
- Foreign keys match documented relationships.
- No PostgreSQL ENUMs (none documented).
- No CHECK constraints.
- No undocumented indexes.
- No undocumented audit/helper fields.
- SQL applied successfully to an empty PostgreSQL 16 database (46 tables created).

