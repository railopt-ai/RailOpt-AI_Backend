package com.railopt.ai.payload.conflict;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateScheduleConflictRequest(
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
