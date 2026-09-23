package com.railopt.ai.dto.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TrackResponse(
    @JsonProperty("id")
    String id,

    @JsonProperty("section_id")
    String sectionId,

    @JsonProperty("code")
    String code,

    @JsonProperty("name")
    String name,

    @JsonProperty("status")
    String status
) {}
