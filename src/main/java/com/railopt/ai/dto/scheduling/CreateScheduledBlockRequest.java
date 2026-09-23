package com.railopt.ai.dto.scheduling;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record CreateScheduledBlockRequest(
    @NotNull
    @JsonProperty("block_section_id")
    UUID blockSectionId,

    @JsonProperty("block_code")
    String blockCode,

    @JsonProperty("status")
    String status,

    @NotNull
    @JsonProperty("planned_start_at")
    LocalDateTime plannedStartAt,

    @NotNull
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
