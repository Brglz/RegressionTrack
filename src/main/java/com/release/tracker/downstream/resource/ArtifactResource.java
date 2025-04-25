package com.release.tracker.downstream.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtifactResource {

    @JsonProperty("file_type")
    private String fileType;

    private long size;

    @JsonProperty("filename")
    private String fileName;

    @JsonProperty("file_format")
    private String fileFormat;

}
