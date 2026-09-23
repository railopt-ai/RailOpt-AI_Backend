package com.railopt.ai.payload.scheduling;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateScheduledBlockResourceRequest(
    @NotNull
    @JsonProperty("resource_id")
    UUID resourceId,

    @NotNull
    @Min(1)
    @JsonProperty("quantity_allocated")
    Integer quantityAllocated,

    @JsonProperty("allocated_start_at")
    LocalDateTime allocatedStartAt,

    @JsonProperty("allocated_end_at")
    LocalDateTime allocatedEndAt
) {}
