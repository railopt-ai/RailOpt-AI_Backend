package com.railopt.ai.dto.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateSectionRequest(
    @NotBlank
    @JsonProperty("name")
    String name,

    @JsonProperty("code")
    String code
) {}
