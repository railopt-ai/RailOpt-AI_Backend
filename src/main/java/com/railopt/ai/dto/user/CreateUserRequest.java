package com.railopt.ai.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank
    @Email
    @JsonProperty("email")
    String email,

    @NotBlank
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
