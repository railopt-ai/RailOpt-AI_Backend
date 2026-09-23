package com.railopt.ai.dto.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateAssetRequest(
    @NotBlank
    @JsonProperty("asset_type_id")
    String assetTypeId,

    @JsonProperty("track_section_id")
    String trackSectionId,

    @JsonProperty("station_id")
    String stationId,

    @JsonProperty("name")
    String name,

    @JsonProperty("code")
    String code
) {}
