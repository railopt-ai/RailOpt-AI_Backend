package com.railopt.ai.payload.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRoleRequest(
    @NotBlank
    @JsonProperty("role")
    String role
) {}
