package com.railopt.ai.payload.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserStatusRequest(
    @NotBlank
    @JsonProperty("status")
    String status
) {}
