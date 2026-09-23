package com.railopt.ai.payload.scheduling;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateScheduledBlockRequest(
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
