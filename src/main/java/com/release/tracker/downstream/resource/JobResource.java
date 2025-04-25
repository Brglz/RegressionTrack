package com.release.tracker.downstream.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobResource {
    private String id;
    private String status;
    private String name;
    private PipelineResource pipeline;
    private List<ArtifactResource> artifact;
}
