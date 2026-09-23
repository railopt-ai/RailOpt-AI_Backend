package com.railopt.ai.payload.scheduling;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduledBlockResourceResponse(
    @JsonProperty("scheduled_block_id")
    UUID scheduledBlockId,

    @JsonProperty("resource_id")
    UUID resourceId,

    @JsonProperty("quantity_allocated")
    Integer quantityAllocated,

    @JsonProperty("allocated_start_at")
    LocalDateTime allocatedStartAt,

    @JsonProperty("allocated_end_at")
    LocalDateTime allocatedEndAt
) {}
