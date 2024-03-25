package com.trafik.tracker.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LineDataApiResponse<T> {
    @JsonProperty("StatusCode")
    private int statusCode;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("ExecutionTime")
    private int executionTime;
    @JsonProperty("ResponseData")
    private ResponseData<T> responseData;

}
