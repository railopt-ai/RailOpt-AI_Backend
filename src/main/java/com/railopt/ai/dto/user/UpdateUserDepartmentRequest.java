package com.railopt.ai.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserDepartmentRequest(
    @NotBlank
    @JsonProperty("department_id")
    String departmentId
) {}
