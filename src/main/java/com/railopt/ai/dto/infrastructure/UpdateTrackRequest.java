package com.railopt.ai.dto.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateTrackRequest(
    @JsonProperty("name")
    String name,

    @JsonProperty("code")
    String code
) {}
