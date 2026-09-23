package com.railopt.ai.payload.user;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserListResponse(
    @JsonProperty("users")
    List<UserResponse> users,

    @JsonProperty("total")
    Integer total,

    @JsonProperty("page")
    Integer page,

    @JsonProperty("size")
    Integer size
) {}
