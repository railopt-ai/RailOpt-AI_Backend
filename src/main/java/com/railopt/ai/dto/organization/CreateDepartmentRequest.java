package com.railopt.ai.dto.organization;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDepartmentRequest(
    @NotBlank
    @Size(min = 2, max = 100)
    @JsonProperty("name")
    String name
) {}
