package com.railopt.ai.payload.train;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TrainRunTrackSectionResponse(
    @JsonProperty("track_section_id")
    UUID trackSectionId,

    @JsonProperty("sequence_no")
    Integer sequenceNo,

    @JsonProperty("planned_entry_at")
    LocalDateTime plannedEntryAt,

    @JsonProperty("planned_exit_at")
    LocalDateTime plannedExitAt
) {}
