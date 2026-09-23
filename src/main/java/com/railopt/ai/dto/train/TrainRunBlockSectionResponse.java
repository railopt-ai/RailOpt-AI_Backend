package com.railopt.ai.dto.train;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TrainRunBlockSectionResponse(
    @JsonProperty("block_section_id")
    UUID blockSectionId,

    @JsonProperty("sequence_no")
    Integer sequenceNo,

    @JsonProperty("planned_entry_at")
    LocalDateTime plannedEntryAt,

    @JsonProperty("planned_exit_at")
    LocalDateTime plannedExitAt
) {}
