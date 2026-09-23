package com.railopt.ai.payload.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BlockResponse(
    @JsonProperty("id")
    String id,

    @JsonProperty("track_id")
    String trackId,

    @JsonProperty("code")
    String code,

    @JsonProperty("name")
    String name,

    @JsonProperty("status")
    String status
) {}
