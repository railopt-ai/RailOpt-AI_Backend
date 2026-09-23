package com.railopt.ai.payload.maintenance;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateMaintenanceTaskRequest(
    @JsonProperty("maintenance_type")
    String maintenanceType,

    @JsonProperty("scheduled_start")
    LocalDateTime scheduledStart,

    @JsonProperty("scheduled_end")
    LocalDateTime scheduledEnd,

    @JsonProperty("description")
    String description
) {}
