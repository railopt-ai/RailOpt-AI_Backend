package com.railopt.ai.payload.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public record AdminUpdateUserRequest(
    @Size(min = 2, max = 100)
    @JsonProperty("name")
    String name,

    @JsonProperty("phone")
    String phone,

    @JsonProperty("role")
    String role,

    @JsonProperty("department_id")
    String departmentId
) {}
