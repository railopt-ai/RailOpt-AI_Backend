package com.railopt.ai.dto.train;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateTrainRunBlockSectionRequest(
    @NotNull
    @JsonProperty("block_section_id")
    UUID blockSectionId,

    @NotNull
    @Min(1)
    @JsonProperty("sequence_no")
    Integer sequenceNo,

    @JsonProperty("planned_entry_at")
    LocalDateTime plannedEntryAt,

    @JsonProperty("planned_exit_at")
    LocalDateTime plannedExitAt
) {}
