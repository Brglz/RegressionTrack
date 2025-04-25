package com.release.tracker.downstream.client;

import com.release.tracker.downstream.config.GitlabFeignConfig;
import com.release.tracker.downstream.resource.JobResource;
import com.release.tracker.downstream.resource.PipelineResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gitlab", url = "https://gitlab.paysafe.cloud/api/v4/", configuration = GitlabFeignConfig.class)
public interface GitlabClient {

    @PostMapping("/projects/{projectId}/pipeline")
    PipelineResource createPipeline(@PathVariable String projectId, @RequestBody PipelineResource body);

    @GetMapping("/projects/{projectId}/pipeline")
    PipelineResource getPipeline(@PathVariable String projectId);

//    @GetMapping("/projects/{projectId}/pipelines/{pipelineId}/jobs")
//    List<Map<String, Object>> getJobs(@PathVariable String projectId, @PathVariable String pipelineId); //stel maybe not needed

    @PostMapping("/projects/{projectId}/jobs/{jobId}/play")
    JobResource runJob(@PathVariable String projectId, @PathVariable String jobId);

    @GetMapping("/projects/{projectId}/jobs/{jobId}")
    Object getJob(@PathVariable String projectId, @PathVariable String jobId);


}
