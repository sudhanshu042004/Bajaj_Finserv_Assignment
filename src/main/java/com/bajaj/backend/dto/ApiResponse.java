package com.bajaj.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    @JsonProperty("is_success")
    private boolean success;

    @JsonProperty("official_email")
    private String officialEmail;

    private Object data;
}
