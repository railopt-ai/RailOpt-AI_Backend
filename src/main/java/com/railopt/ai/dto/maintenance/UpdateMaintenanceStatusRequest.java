package com.railopt.ai.dto.maintenance;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UpdateMaintenanceStatusRequest(
    @NotBlank
    @JsonProperty("status")
    String status
) {}
