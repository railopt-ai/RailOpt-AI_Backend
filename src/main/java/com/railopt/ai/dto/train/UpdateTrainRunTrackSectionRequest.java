package com.railopt.ai.dto.train;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

public record UpdateTrainRunTrackSectionRequest(
    @Min(1)
    @JsonProperty("sequence_no")
    Integer sequenceNo,

    @JsonProperty("planned_entry_at")
    LocalDateTime plannedEntryAt,

    @JsonProperty("planned_exit_at")
    LocalDateTime plannedExitAt
) {}
