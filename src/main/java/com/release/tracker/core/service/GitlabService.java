package com.release.tracker.core.service;

import com.release.tracker.downstream.client.GitlabClient;
import com.release.tracker.downstream.resource.JobResource;
import com.release.tracker.downstream.resource.PipelineResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitlabService {

    private final GitlabClient gitLabClient;

    @Autowired
    public GitlabService(GitlabClient gitLabClient) {
        this.gitLabClient = gitLabClient;
    }

    public PipelineResource createPipeline(String projectId) {
        return gitLabClient.createPipeline(projectId, createPipelineRequestResource());
    }

    public JobResource runJob(String projectId, String jobId) {
        return gitLabClient.runJob(projectId, jobId);
    }

    private PipelineResource createPipelineRequestResource() {
        return new PipelineResource();
    }

//    public void triggerJob(String projectId, String serviceName) {
//        String jobName = "resttest-" + serviceName + "-qa-mon-regression";
//
//        Map<String, Object> pipeline = gitLabClient.createPipeline(projectId, Map.of("ref", "master"));
//        String pipelineId = pipeline.get("id").toString() ;
//
//        List<Map<String, Object>> jobs = gitLabClient.getJobs(projectId, pipelineId);
//
//        jobs.stream()
//                .filter(job -> jobName.equals(job.get("name")))
//                .findFirst()
//                .ifPresent(job -> {
//                    String jobId = job.get("id").toString();
//                    gitLabClient.createJob(projectId, jobId);
//                });
//    }
}
