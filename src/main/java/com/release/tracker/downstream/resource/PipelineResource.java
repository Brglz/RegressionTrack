package com.release.tracker.downstream.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PipelineResource {
    private String id;
    private String status;
    private String name;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private List<JobResource> jobs;

    // intentionally left with for loop as the jobs can be a lot and this is the best performance wise
    public JobResource getJobByName(String jobName) {
        if (jobs == null || jobName == null) {
            return null;
        }
        for (JobResource job : jobs) {
            if (jobName.equals(job.getName())) {
                return job;
            }
        }
        return null;
    }
}
