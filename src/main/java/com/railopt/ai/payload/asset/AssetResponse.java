package com.railopt.ai.payload.asset;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AssetResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("asset_type_id")
    UUID assetTypeId,

    @JsonProperty("track_section_id")
    UUID trackSectionId,

    @JsonProperty("station_id")
    UUID stationId,

    @JsonProperty("code")
    String code,

    @JsonProperty("name")
    String name,

    @JsonProperty("status")
    String status,

    @JsonProperty("criticality")
    String criticality
) {}
