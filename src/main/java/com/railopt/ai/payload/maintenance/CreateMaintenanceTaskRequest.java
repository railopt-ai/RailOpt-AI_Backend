package com.railopt.ai.payload.maintenance;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateMaintenanceTaskRequest(
    @NotBlank
    @JsonProperty("asset_id")
    String assetId,

    @NotBlank
    @JsonProperty("maintenance_type")
    String maintenanceType,

    @JsonProperty("scheduled_start")
    LocalDateTime scheduledStart,

    @JsonProperty("scheduled_end")
    LocalDateTime scheduledEnd,

    @JsonProperty("description")
    String description
) {}
