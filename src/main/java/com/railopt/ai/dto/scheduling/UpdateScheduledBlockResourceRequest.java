package com.railopt.ai.dto.scheduling;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

public record UpdateScheduledBlockResourceRequest(
    @Min(1)
    @JsonProperty("quantity_allocated")
    Integer quantityAllocated,

    @JsonProperty("allocated_start_at")
    LocalDateTime allocatedStartAt,

    @JsonProperty("allocated_end_at")
    LocalDateTime allocatedEndAt
) {}
