package com.railopt.ai.payload.maintenance;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MaintenanceTaskResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("asset_id")
    UUID assetId,

    @JsonProperty("task_code")
    String taskCode,

    @JsonProperty("maintenance_type")
    String maintenanceType,

    @JsonProperty("priority")
    String priority,

    @JsonProperty("status")
    String status,

    @JsonProperty("description")
    String description,

    @JsonProperty("scheduled_start")
    LocalDateTime scheduledStart,

    @JsonProperty("scheduled_end")
    LocalDateTime scheduledEnd
) {}
