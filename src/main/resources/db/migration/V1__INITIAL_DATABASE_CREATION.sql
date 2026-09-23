-- =============================================================================
-- RailOpt-AI V1 — Initial PostgreSQL schema
--
-- Source of truth for intended design: docs/database-schema.mmd
-- This file is the actual PostgreSQL implementation of that design.
--
-- Implementation decisions (not specified in the mermaid schema):
--   * Unquoted identifiers: PostgreSQL stores them in lowercase
--     (USERS -> users). Names otherwise match the schema exactly.
--   * VARCHAR columns have no length: the schema does not define maxima.
--     Do not treat unbounded VARCHAR as a business length rule.
--   * DECIMAL is implemented as PostgreSQL NUMERIC with no precision/scale.
--   * TIMESTAMP is TIMESTAMP WITHOUT TIME ZONE (schema is not TIMESTAMPTZ).
--   * TIME is TIME WITHOUT TIME ZONE.
--   * No PostgreSQL ENUM types: the schema does not list allowed values.
--   * No CHECK constraints: none are documented.
--   * No extra indexes: only PRIMARY KEY and UNIQUE (UK) indexes.
--   * Foreign keys use the PostgreSQL default (NO ACTION). No CASCADE/SET NULL.
--   * PK columns and composite-PK columns are NOT NULL.
--   * UK columns are UNIQUE NOT NULL (unique identifiers in the schema).
--   * Other columns are NULLABLE unless listed as a required FK below.
--   * Required FKs follow identifying parent cardinality (||--o{ / ||--o|),
--     except documented nullable FKs:
--       role_enrollments.reviewed_by
--       assets.track_section_id
--       assets.station_id
--       maintenance_tasks.defect_id
--       schedule_conflicts.scheduled_task_id
--       schedule_conflicts.scheduled_block_id
--       schedule_conflicts.train_run_id
--       schedule_conflicts.resource_id
--       schedule_conflicts.track_section_id
--       schedule_conflicts.block_section_id
--   * No UUID default generation (not specified).
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. AUTHENTICATION & AUTHORIZATION
-- -----------------------------------------------------------------------------

CREATE TABLE users (
    id                  UUID PRIMARY KEY,
    google_subject_id   VARCHAR NOT NULL UNIQUE,
    email               VARCHAR NOT NULL UNIQUE,
    name                VARCHAR,
    profile_picture_url TEXT,
    status              VARCHAR,
    last_login_at       TIMESTAMP,
    created_at          TIMESTAMP,
    updated_at          TIMESTAMP
);

CREATE TABLE roles (
    id          UUID PRIMARY KEY,
    code        VARCHAR NOT NULL UNIQUE,
    name        VARCHAR,
    description TEXT,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE user_roles (
    user_id     UUID NOT NULL,
    role_id     UUID NOT NULL,
    assigned_at TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE departments (
    id          UUID PRIMARY KEY,
    code        VARCHAR NOT NULL UNIQUE,
    name        VARCHAR,
    description TEXT,
    status      VARCHAR,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE role_enrollments (
    id             UUID PRIMARY KEY,
    user_id        UUID NOT NULL,
    role_id        UUID NOT NULL,
    status         VARCHAR,
    request_reason TEXT,
    requested_at   TIMESTAMP,
    reviewed_by    UUID,
    reviewed_at    TIMESTAMP,
    review_reason  TEXT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (reviewed_by) REFERENCES users (id)
);

CREATE TABLE user_departments (
    user_id        UUID NOT NULL,
    department_id  UUID NOT NULL,
    joined_at      TIMESTAMP,
    left_at        TIMESTAMP,
    PRIMARY KEY (user_id, department_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (department_id) REFERENCES departments (id)
);

-- -----------------------------------------------------------------------------
-- 2. RAILWAY ORGANIZATION
-- -----------------------------------------------------------------------------

CREATE TABLE railways (
    id          UUID PRIMARY KEY,
    code        VARCHAR NOT NULL UNIQUE,
    name        VARCHAR,
    description TEXT,
    status      VARCHAR,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE railway_zones (
    id          UUID PRIMARY KEY,
    railway_id  UUID NOT NULL,
    code        VARCHAR NOT NULL UNIQUE,
    name        VARCHAR,
    description TEXT,
    status      VARCHAR,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (railway_id) REFERENCES railways (id)
);

CREATE TABLE railway_divisions (
    id          UUID PRIMARY KEY,
    zone_id     UUID NOT NULL,
    code        VARCHAR NOT NULL UNIQUE,
    name        VARCHAR,
    description TEXT,
    status      VARCHAR,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (zone_id) REFERENCES railway_zones (id)
);

-- -----------------------------------------------------------------------------
-- 3. INFRASTRUCTURE
-- -----------------------------------------------------------------------------

CREATE TABLE stations (
    id           UUID PRIMARY KEY,
    division_id  UUID NOT NULL,
    code         VARCHAR NOT NULL UNIQUE,
    name         VARCHAR,
    latitude     NUMERIC,
    longitude    NUMERIC,
    station_type VARCHAR,
    status       VARCHAR,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,
    FOREIGN KEY (division_id) REFERENCES railway_divisions (id)
);

CREATE TABLE tracks (
    id                      UUID PRIMARY KEY,
    division_id             UUID NOT NULL,
    code                    VARCHAR NOT NULL UNIQUE,
    name                    VARCHAR,
    track_type              VARCHAR,
    gauge                   VARCHAR,
    electrification_status  VARCHAR,
    status                  VARCHAR,
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP,
    FOREIGN KEY (division_id) REFERENCES railway_divisions (id)
);

CREATE TABLE track_sections (
    id              UUID PRIMARY KEY,
    track_id        UUID NOT NULL,
    from_station_id UUID NOT NULL,
    to_station_id   UUID NOT NULL,
    code            VARCHAR NOT NULL UNIQUE,
    length_km       NUMERIC,
    status          VARCHAR,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    FOREIGN KEY (track_id) REFERENCES tracks (id),
    FOREIGN KEY (from_station_id) REFERENCES stations (id),
    FOREIGN KEY (to_station_id) REFERENCES stations (id)
);

CREATE TABLE block_sections (
    id              UUID PRIMARY KEY,
    division_id     UUID NOT NULL,
    from_station_id UUID NOT NULL,
    to_station_id   UUID NOT NULL,
    code            VARCHAR NOT NULL UNIQUE,
    block_system    VARCHAR,
    status          VARCHAR,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    FOREIGN KEY (division_id) REFERENCES railway_divisions (id),
    FOREIGN KEY (from_station_id) REFERENCES stations (id),
    FOREIGN KEY (to_station_id) REFERENCES stations (id)
);

CREATE TABLE block_availability_windows (
    id                 UUID PRIMARY KEY,
    block_section_id   UUID NOT NULL,
    available_from     TIMESTAMP,
    available_until    TIMESTAMP,
    availability_type  VARCHAR,
    status             VARCHAR,
    restriction_reason TEXT,
    created_at         TIMESTAMP,
    updated_at         TIMESTAMP,
    FOREIGN KEY (block_section_id) REFERENCES block_sections (id)
);

CREATE TABLE corridors (
    id           UUID PRIMARY KEY,
    division_id  UUID NOT NULL,
    code         VARCHAR NOT NULL UNIQUE,
    name         VARCHAR,
    description  TEXT,
    status       VARCHAR,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,
    FOREIGN KEY (division_id) REFERENCES railway_divisions (id)
);

CREATE TABLE corridor_track_sections (
    corridor_id      UUID NOT NULL,
    track_section_id UUID NOT NULL,
    sequence_no      INTEGER,
    PRIMARY KEY (corridor_id, track_section_id),
    FOREIGN KEY (corridor_id) REFERENCES corridors (id),
    FOREIGN KEY (track_section_id) REFERENCES track_sections (id)
);

CREATE TABLE corridor_block_sections (
    corridor_id      UUID NOT NULL,
    block_section_id UUID NOT NULL,
    sequence_no      INTEGER,
    PRIMARY KEY (corridor_id, block_section_id),
    FOREIGN KEY (corridor_id) REFERENCES corridors (id),
    FOREIGN KEY (block_section_id) REFERENCES block_sections (id)
);

CREATE TABLE asset_types (
    id            UUID PRIMARY KEY,
    department_id UUID NOT NULL,
    code          VARCHAR NOT NULL UNIQUE,
    name          VARCHAR,
    description   TEXT,
    status        VARCHAR,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments (id)
);

CREATE TABLE assets (
    id                         UUID PRIMARY KEY,
    asset_type_id              UUID NOT NULL,
    track_section_id           UUID,
    station_id                 UUID,
    code                       VARCHAR NOT NULL UNIQUE,
    name                       VARCHAR,
    location_km                NUMERIC,
    installation_date          DATE,
    status                     VARCHAR,
    criticality                VARCHAR,
    condition_status           VARCHAR,
    availability_percentage    NUMERIC,
    last_maintenance_at        TIMESTAMP,
    next_maintenance_due_at    TIMESTAMP,
    created_at                 TIMESTAMP,
    updated_at                 TIMESTAMP,
    FOREIGN KEY (asset_type_id) REFERENCES asset_types (id),
    FOREIGN KEY (track_section_id) REFERENCES track_sections (id),
    FOREIGN KEY (station_id) REFERENCES stations (id)
);

-- -----------------------------------------------------------------------------
-- 4. MAINTENANCE
-- -----------------------------------------------------------------------------

CREATE TABLE defects (
    id           UUID PRIMARY KEY,
    asset_id     UUID NOT NULL,
    reported_by  UUID NOT NULL,
    defect_code  VARCHAR NOT NULL UNIQUE,
    defect_type  VARCHAR,
    severity     VARCHAR,
    status       VARCHAR,
    description  TEXT,
    detected_at  TIMESTAMP,
    due_at       TIMESTAMP,
    resolved_at  TIMESTAMP,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,
    FOREIGN KEY (asset_id) REFERENCES assets (id),
    FOREIGN KEY (reported_by) REFERENCES users (id)
);

CREATE TABLE maintenance_types (
    id                    UUID PRIMARY KEY,
    department_id         UUID NOT NULL,
    code                  VARCHAR NOT NULL UNIQUE,
    name                  VARCHAR,
    description           TEXT,
    maintenance_category  VARCHAR,
    created_at            TIMESTAMP,
    updated_at            TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments (id)
);

CREATE TABLE maintenance_tasks (
    id                      UUID PRIMARY KEY,
    asset_id                UUID NOT NULL,
    defect_id               UUID,
    maintenance_type_id     UUID NOT NULL,
    assigned_department_id  UUID NOT NULL,
    assigned_by             UUID NOT NULL,
    task_code               VARCHAR NOT NULL UNIQUE,
    status                  VARCHAR,
    priority                VARCHAR,
    description             TEXT,
    estimated_duration_hours NUMERIC,
    actual_duration_hours    NUMERIC,
    requested_at            TIMESTAMP,
    scheduled_at            TIMESTAMP,
    completed_at            TIMESTAMP,
    deadline_at             TIMESTAMP,
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP,
    FOREIGN KEY (asset_id) REFERENCES assets (id),
    FOREIGN KEY (defect_id) REFERENCES defects (id),
    FOREIGN KEY (maintenance_type_id) REFERENCES maintenance_types (id),
    FOREIGN KEY (assigned_department_id) REFERENCES departments (id),
    FOREIGN KEY (assigned_by) REFERENCES users (id)
);

CREATE TABLE maintenance_resources (
    id            UUID PRIMARY KEY,
    department_id UUID NOT NULL,
    code          VARCHAR NOT NULL UNIQUE,
    name          VARCHAR,
    resource_type VARCHAR,
    capacity      INTEGER,
    status        VARCHAR,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments (id)
);

CREATE TABLE maintenance_task_resources (
    maintenance_task_id UUID NOT NULL,
    resource_id         UUID NOT NULL,
    required_quantity   INTEGER,
    PRIMARY KEY (maintenance_task_id, resource_id),
    FOREIGN KEY (maintenance_task_id) REFERENCES maintenance_tasks (id),
    FOREIGN KEY (resource_id) REFERENCES maintenance_resources (id)
);

CREATE TABLE resource_availability (
    id                 UUID PRIMARY KEY,
    resource_id        UUID NOT NULL,
    available_from     TIMESTAMP,
    available_until    TIMESTAMP,
    available_quantity INTEGER,
    status             VARCHAR,
    created_at         TIMESTAMP,
    FOREIGN KEY (resource_id) REFERENCES maintenance_resources (id)
);

CREATE TABLE maintenance_task_block_requirements (
    maintenance_task_id       UUID NOT NULL,
    block_section_id          UUID NOT NULL,
    requirement_type          VARCHAR,
    requires_full_block       BOOLEAN,
    requires_power_block      BOOLEAN,
    requires_signal_block     BOOLEAN,
    requires_speed_restriction BOOLEAN,
    safety_buffer_minutes     INTEGER,
    minimum_duration_hours    NUMERIC,
    created_at                TIMESTAMP,
    PRIMARY KEY (maintenance_task_id, block_section_id),
    FOREIGN KEY (maintenance_task_id) REFERENCES maintenance_tasks (id),
    FOREIGN KEY (block_section_id) REFERENCES block_sections (id)
);

-- -----------------------------------------------------------------------------
-- 5. TRAIN & OPERATIONS
-- -----------------------------------------------------------------------------

CREATE TABLE train_types (
    id             UUID PRIMARY KEY,
    code           VARCHAR NOT NULL UNIQUE,
    name           VARCHAR,
    description    TEXT,
    category       VARCHAR,
    priority_class VARCHAR,
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP
);

CREATE TABLE trains (
    id            UUID PRIMARY KEY,
    train_type_id UUID NOT NULL,
    train_number  VARCHAR NOT NULL UNIQUE,
    train_name    VARCHAR,
    operator      VARCHAR,
    status        VARCHAR,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    FOREIGN KEY (train_type_id) REFERENCES train_types (id)
);

CREATE TABLE train_routes (
    id                    UUID PRIMARY KEY,
    train_id              UUID NOT NULL,
    route_code            VARCHAR NOT NULL UNIQUE,
    name                  VARCHAR,
    origin_station_id     UUID NOT NULL,
    destination_station_id UUID NOT NULL,
    total_stations        INTEGER,
    created_at            TIMESTAMP,
    updated_at            TIMESTAMP,
    FOREIGN KEY (train_id) REFERENCES trains (id),
    FOREIGN KEY (origin_station_id) REFERENCES stations (id),
    FOREIGN KEY (destination_station_id) REFERENCES stations (id)
);

CREATE TABLE train_route_stops (
    id                        UUID PRIMARY KEY,
    train_route_id            UUID NOT NULL,
    station_id                UUID NOT NULL,
    sequence_no               INTEGER,
    arrival_offset_minutes    INTEGER,
    departure_offset_minutes  INTEGER,
    created_at                TIMESTAMP,
    FOREIGN KEY (train_route_id) REFERENCES train_routes (id),
    FOREIGN KEY (station_id) REFERENCES stations (id)
);

CREATE TABLE train_schedules (
    id              UUID PRIMARY KEY,
    train_id        UUID NOT NULL,
    train_route_id  UUID NOT NULL,
    schedule_code   VARCHAR NOT NULL UNIQUE,
    valid_from      DATE,
    valid_until     DATE,
    operating_days  VARCHAR,
    departure_time  TIME,
    arrival_time    TIME,
    status          VARCHAR,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    FOREIGN KEY (train_id) REFERENCES trains (id),
    FOREIGN KEY (train_route_id) REFERENCES train_routes (id)
);

CREATE TABLE train_runs (
    id                 UUID PRIMARY KEY,
    train_id           UUID NOT NULL,
    train_route_id     UUID NOT NULL,
    train_schedule_id  UUID NOT NULL,
    run_code           VARCHAR NOT NULL UNIQUE,
    operation_date     DATE,
    status             VARCHAR,
    planned_start_at   TIMESTAMP,
    planned_end_at     TIMESTAMP,
    actual_start_at    TIMESTAMP,
    actual_end_at      TIMESTAMP,
    created_at         TIMESTAMP,
    updated_at         TIMESTAMP,
    FOREIGN KEY (train_id) REFERENCES trains (id),
    FOREIGN KEY (train_route_id) REFERENCES train_routes (id),
    FOREIGN KEY (train_schedule_id) REFERENCES train_schedules (id)
);

CREATE TABLE train_run_stops (
    id                    UUID PRIMARY KEY,
    train_run_id          UUID NOT NULL,
    station_id            UUID NOT NULL,
    sequence_no           INTEGER,
    planned_arrival_at    TIMESTAMP,
    planned_departure_at  TIMESTAMP,
    actual_arrival_at     TIMESTAMP,
    actual_departure_at   TIMESTAMP,
    FOREIGN KEY (train_run_id) REFERENCES train_runs (id),
    FOREIGN KEY (station_id) REFERENCES stations (id)
);

CREATE TABLE train_run_track_sections (
    train_run_id     UUID NOT NULL,
    track_section_id UUID NOT NULL,
    sequence_no      INTEGER,
    planned_entry_at TIMESTAMP,
    planned_exit_at  TIMESTAMP,
    PRIMARY KEY (train_run_id, track_section_id),
    FOREIGN KEY (train_run_id) REFERENCES train_runs (id),
    FOREIGN KEY (track_section_id) REFERENCES track_sections (id)
);

CREATE TABLE train_run_block_sections (
    train_run_id     UUID NOT NULL,
    block_section_id UUID NOT NULL,
    sequence_no      INTEGER,
    planned_entry_at TIMESTAMP,
    planned_exit_at  TIMESTAMP,
    PRIMARY KEY (train_run_id, block_section_id),
    FOREIGN KEY (train_run_id) REFERENCES train_runs (id),
    FOREIGN KEY (block_section_id) REFERENCES block_sections (id)
);

-- -----------------------------------------------------------------------------
-- 6. TRAFFIC / GOODS FORECAST
-- -----------------------------------------------------------------------------

CREATE TABLE traffic_forecasts (
    id               UUID PRIMARY KEY,
    division_id      UUID NOT NULL,
    forecast_type    VARCHAR,
    forecast_date    DATE,
    horizon_start_at TIMESTAMP,
    horizon_end_at   TIMESTAMP,
    status           VARCHAR,
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP,
    FOREIGN KEY (division_id) REFERENCES railway_divisions (id)
);

CREATE TABLE traffic_forecast_items (
    id                        UUID PRIMARY KEY,
    forecast_id               UUID NOT NULL,
    block_section_id          UUID NOT NULL,
    time_window_start         TIMESTAMP,
    time_window_end           TIMESTAMP,
    train_category            VARCHAR,
    expected_train_count      INTEGER,
    probability               NUMERIC,
    expected_passenger_trains INTEGER,
    expected_goods_trains     INTEGER,
    created_at                TIMESTAMP,
    FOREIGN KEY (forecast_id) REFERENCES traffic_forecasts (id),
    FOREIGN KEY (block_section_id) REFERENCES block_sections (id)
);

-- -----------------------------------------------------------------------------
-- 7. SCHEDULING / BLOCK PLANNING
-- -----------------------------------------------------------------------------

CREATE TABLE scheduling_runs (
    id                       UUID PRIMARY KEY,
    created_by               UUID NOT NULL,
    run_code                 VARCHAR NOT NULL UNIQUE,
    algorithm_type           VARCHAR,
    status                   VARCHAR,
    planning_start_at        TIMESTAMP,
    planning_end_at          TIMESTAMP,
    started_at               TIMESTAMP,
    completed_at             TIMESTAMP,
    objective_description    TEXT,
    objective_weights        JSONB,
    total_tasks_considered   INTEGER,
    total_tasks_scheduled    INTEGER,
    total_tasks_unscheduled  INTEGER,
    error_message            TEXT,
    created_at               TIMESTAMP,
    updated_at               TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE scheduling_run_tasks (
    scheduling_run_id        UUID NOT NULL,
    maintenance_task_id      UUID NOT NULL,
    consideration_status     VARCHAR,
    unscheduled_reason       VARCHAR,
    predicted_duration_hours NUMERIC,
    effective_duration_hours NUMERIC,
    priority_score           NUMERIC,
    risk_score               NUMERIC,
    created_at               TIMESTAMP,
    PRIMARY KEY (scheduling_run_id, maintenance_task_id),
    FOREIGN KEY (scheduling_run_id) REFERENCES scheduling_runs (id),
    FOREIGN KEY (maintenance_task_id) REFERENCES maintenance_tasks (id)
);

CREATE TABLE scheduled_tasks (
    id                     UUID PRIMARY KEY,
    scheduling_run_id      UUID NOT NULL,
    maintenance_task_id    UUID NOT NULL,
    status                 VARCHAR,
    planned_start_at       TIMESTAMP,
    planned_end_at         TIMESTAMP,
    planned_duration_hours NUMERIC,
    objective_contribution NUMERIC,
    scheduling_reason      TEXT,
    created_at             TIMESTAMP,
    updated_at             TIMESTAMP,
    FOREIGN KEY (scheduling_run_id) REFERENCES scheduling_runs (id),
    FOREIGN KEY (maintenance_task_id) REFERENCES maintenance_tasks (id)
);

CREATE TABLE scheduled_blocks (
    id                   UUID PRIMARY KEY,
    scheduling_run_id    UUID NOT NULL,
    scheduled_task_id    UUID NOT NULL,
    block_section_id     UUID NOT NULL,
    block_code           VARCHAR,
    status               VARCHAR,
    planned_start_at     TIMESTAMP,
    planned_end_at       TIMESTAMP,
    duration_minutes     INTEGER,
    full_block           BOOLEAN,
    power_disconnection  BOOLEAN,
    train_impact_score   NUMERIC,
    asset_impact_score   NUMERIC,
    scheduling_reason    TEXT,
    created_at           TIMESTAMP,
    updated_at           TIMESTAMP,
    FOREIGN KEY (scheduling_run_id) REFERENCES scheduling_runs (id),
    FOREIGN KEY (scheduled_task_id) REFERENCES scheduled_tasks (id),
    FOREIGN KEY (block_section_id) REFERENCES block_sections (id)
);

CREATE TABLE scheduled_block_resources (
    scheduled_block_id UUID NOT NULL,
    resource_id        UUID NOT NULL,
    quantity_allocated INTEGER,
    allocated_start_at TIMESTAMP,
    allocated_end_at   TIMESTAMP,
    PRIMARY KEY (scheduled_block_id, resource_id),
    FOREIGN KEY (scheduled_block_id) REFERENCES scheduled_blocks (id),
    FOREIGN KEY (resource_id) REFERENCES maintenance_resources (id)
);

CREATE TABLE schedule_conflicts (
    id                        UUID PRIMARY KEY,
    scheduling_run_id         UUID NOT NULL,
    scheduled_task_id         UUID,
    scheduled_block_id        UUID,
    train_run_id              UUID,
    resource_id               UUID,
    track_section_id          UUID,
    block_section_id          UUID,
    conflict_type             VARCHAR,
    severity                  VARCHAR,
    status                    VARCHAR,
    description               TEXT,
    conflict_duration_minutes INTEGER,
    conflict_start_at         TIMESTAMP,
    conflict_end_at           TIMESTAMP,
    created_at                TIMESTAMP,
    resolved_at               TIMESTAMP,
    FOREIGN KEY (scheduling_run_id) REFERENCES scheduling_runs (id),
    FOREIGN KEY (scheduled_task_id) REFERENCES scheduled_tasks (id),
    FOREIGN KEY (scheduled_block_id) REFERENCES scheduled_blocks (id),
    FOREIGN KEY (train_run_id) REFERENCES train_runs (id),
    FOREIGN KEY (resource_id) REFERENCES maintenance_resources (id),
    FOREIGN KEY (track_section_id) REFERENCES track_sections (id),
    FOREIGN KEY (block_section_id) REFERENCES block_sections (id)
);

-- -----------------------------------------------------------------------------
-- 8. SCHEDULING METRICS
-- -----------------------------------------------------------------------------

CREATE TABLE schedule_metrics (
    id                                       UUID PRIMARY KEY,
    scheduling_run_id                        UUID NOT NULL,
    total_tasks                              INTEGER,
    scheduled_tasks                          INTEGER,
    unscheduled_tasks                        INTEGER,
    total_blocks                             INTEGER,
    total_block_hours                        INTEGER,
    train_conflicts                          INTEGER,
    resource_conflicts                       INTEGER,
    safety_conflicts                         INTEGER,
    estimated_train_delay_minutes            NUMERIC,
    estimated_asset_downtime_hours           NUMERIC,
    estimated_asset_availability_percentage  NUMERIC,
    resource_utilization_percentage          NUMERIC,
    maintenance_completion_percentage        NUMERIC,
    total_objective_score                    NUMERIC,
    calculated_at                            TIMESTAMP,
    FOREIGN KEY (scheduling_run_id) REFERENCES scheduling_runs (id)
);

CREATE TABLE schedule_baseline_metrics (
    id                                       UUID PRIMARY KEY,
    scheduling_run_id                        UUID NOT NULL,
    total_tasks                              INTEGER,
    tasks_without_optimized_scheduling       INTEGER,
    estimated_block_hours                    INTEGER,
    estimated_train_delay_minutes            NUMERIC,
    estimated_asset_downtime_hours           NUMERIC,
    estimated_asset_availability_percentage  NUMERIC,
    resource_utilization_percentage          NUMERIC,
    calculated_at                            TIMESTAMP,
    FOREIGN KEY (scheduling_run_id) REFERENCES scheduling_runs (id)
);

-- -----------------------------------------------------------------------------
-- 9. ML PREDICTIONS
-- -----------------------------------------------------------------------------

CREATE TABLE scheduling_predictions (
    id                  UUID PRIMARY KEY,
    scheduling_run_id   UUID NOT NULL,
    maintenance_task_id UUID NOT NULL,
    prediction_type     VARCHAR,
    predicted_value     NUMERIC,
    model_name          VARCHAR,
    model_version       VARCHAR,
    confidence_score    NUMERIC,
    input_features      JSONB,
    predicted_at        TIMESTAMP,
    FOREIGN KEY (scheduling_run_id) REFERENCES scheduling_runs (id),
    FOREIGN KEY (maintenance_task_id) REFERENCES maintenance_tasks (id)
);
