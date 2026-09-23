package com.railopt.ai.payload.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(
    @Size(min = 2, max = 100)
    @JsonProperty("name")
    String name
) {}
