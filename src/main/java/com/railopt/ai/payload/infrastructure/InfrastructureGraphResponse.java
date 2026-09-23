package com.railopt.ai.payload.infrastructure;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record InfrastructureGraphResponse(
    @JsonProperty("nodes")
    List<GraphNode> nodes,

    @JsonProperty("edges")
    List<GraphEdge> edges
) {}
