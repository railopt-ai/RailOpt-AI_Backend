package com.railopt.ai.dto.conflict;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UpdateScheduleConflictStatusRequest(
    @NotBlank
    @JsonProperty("status")
    String status,

    @JsonProperty("resolved_at")
    LocalDateTime resolvedAt
) {}
