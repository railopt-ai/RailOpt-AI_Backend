package com.railopt.ai.payload.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateBlockRequest(
    @NotBlank
    @JsonProperty("track_id")
    String trackId,

    @NotBlank
    @JsonProperty("name")
    String name,

    @JsonProperty("code")
    String code
) {}
