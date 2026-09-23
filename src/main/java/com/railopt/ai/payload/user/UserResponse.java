package com.railopt.ai.payload.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
    @JsonProperty("id")
    String id,

    @JsonProperty("email")
    String email,

    @JsonProperty("name")
    String name,

    @JsonProperty("phone")
    String phone,

    @JsonProperty("role")
    String role,

    @JsonProperty("department_id")
    String departmentId,

    @JsonProperty("status")
    String status
) {}
