package com.railopt.ai.payload.conflict;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleConflictResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("scheduling_run_id")
    UUID schedulingRunId,

    @JsonProperty("scheduled_task_id")
    UUID scheduledTaskId,

    @JsonProperty("scheduled_block_id")
    UUID scheduledBlockId,

    @JsonProperty("train_run_id")
    UUID trainRunId,

    @JsonProperty("resource_id")
    UUID resourceId,

    @JsonProperty("track_section_id")
    UUID trackSectionId,

    @JsonProperty("block_section_id")
    UUID blockSectionId,

    @JsonProperty("conflict_type")
    String conflictType,

    @JsonProperty("severity")
    String severity,

    @JsonProperty("status")
    String status,

    @JsonProperty("description")
    String description,

    @JsonProperty("conflict_duration_minutes")
    Integer conflictDurationMinutes,

    @JsonProperty("conflict_start_at")
    LocalDateTime conflictStartAt,

    @JsonProperty("conflict_end_at")
    LocalDateTime conflictEndAt,

    @JsonProperty("resolved_at")
    LocalDateTime resolvedAt
) {}
