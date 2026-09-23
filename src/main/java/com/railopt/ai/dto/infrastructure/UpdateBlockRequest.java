package com.railopt.ai.dto.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateBlockRequest(
    @JsonProperty("name")
    String name,

    @JsonProperty("code")
    String code
) {}
