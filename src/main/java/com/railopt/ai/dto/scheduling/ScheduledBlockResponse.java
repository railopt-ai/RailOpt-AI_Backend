package com.railopt.ai.dto.scheduling;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduledBlockResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("scheduling_run_id")
    UUID schedulingRunId,

    @JsonProperty("scheduled_task_id")
    UUID scheduledTaskId,

    @JsonProperty("block_section_id")
    UUID blockSectionId,

    @JsonProperty("block_code")
    String blockCode,

    @JsonProperty("status")
    String status,

    @JsonProperty("planned_start_at")
    LocalDateTime plannedStartAt,

    @JsonProperty("planned_end_at")
    LocalDateTime plannedEndAt,

    @JsonProperty("duration_minutes")
    Integer durationMinutes,

    @JsonProperty("full_block")
    Boolean fullBlock,

    @JsonProperty("power_disconnection")
    Boolean powerDisconnection,

    @JsonProperty("train_impact_score")
    Double trainImpactScore,

    @JsonProperty("asset_impact_score")
    Double assetImpactScore,

    @JsonProperty("scheduling_reason")
    String schedulingReason
) {}
