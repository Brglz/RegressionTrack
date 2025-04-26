package com.release.tracker.core.service;

import com.release.tracker.core.enums.ServiceStatus;
import com.release.tracker.core.enums.TestSuiteStatus;
import com.release.tracker.db.entity.Release;
import com.release.tracker.db.entity.ServiceEntity;
import com.release.tracker.db.entity.TestSuite;
import com.release.tracker.db.repository.ReleaseRepository;
import com.release.tracker.db.repository.ServiceRepository;
import com.release.tracker.db.repository.TestSuiteRepository;
import com.release.tracker.downstream.resource.JobResource;
import com.release.tracker.downstream.resource.PipelineResource;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceService {
    private static final String NBX_RESTTEST_ID = "1751";

    private final ServiceRepository serviceRepository;

    private final ReleaseRepository releaseRepository;

    private final TestSuiteRepository testSuiteRepository;

    private final RegressionSimulatorService regressionSimulatorService;

    @Autowired
    private GitlabService gitlabService;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, ReleaseRepository releaseRepository,
                          TestSuiteRepository testSuiteRepository, RegressionSimulatorService regressionSimulatorService) {
        this.serviceRepository = serviceRepository;
        this.releaseRepository = releaseRepository;
        this.testSuiteRepository = testSuiteRepository;
        this.regressionSimulatorService = regressionSimulatorService;
    }

    public List<TestSuite> getTestSuitesByService(UUID serviceId, String sort) {
        List<TestSuite> testSuites = testSuiteRepository.findByServiceId(serviceId);

        if ("asc".equalsIgnoreCase(sort)) {
            testSuites.sort(Comparator.comparing(TestSuite::getStartDate));
        } else if ("desc".equalsIgnoreCase(sort)) {
            testSuites.sort(Comparator.comparing(TestSuite::getStartDate).reversed());
        }

        return testSuites;
    }

    public ServiceEntity getServiceById(UUID id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + id));
    }

    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    public void createServiceForRelease(UUID releaseId, String serviceName, String version, String status) {
        Release release = releaseRepository.findById(releaseId)
                .orElseThrow(() -> new IllegalArgumentException("Release not found"));

        ServiceEntity service = new ServiceEntity();
        service.setId(UUID.randomUUID());
        service.setServiceName(serviceName);
        service.setVersion(version);
        service.setStatus(ServiceStatus.valueOf(status));
        service.setRelease(release);
        serviceRepository.save(service);
    }

    @Transactional
    public void runRegressionTestSuite(UUID serviceId) {
        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

//        PipelineResource pipeline = gitlabService.createPipeline(NBX_RESTTEST_ID);

        TestSuite testSuite = new TestSuite();
        testSuite.setId(UUID.randomUUID());
//        testSuite.setName(pipeline.getName() + "_" + LocalDate.now());
        testSuite.setName("Regression_" + LocalDate.now());
//        testSuite.setStartDate(pipeline.getCreatedAt());
        testSuite.setStartDate(LocalDateTime.now());
        testSuite.setService(service);
        testSuite.setStatus(TestSuiteStatus.IN_PROGRESS);
//        testSuite.setPipelineId(pipeline.getId());
        testSuite.setPipelineId("3430885");
        testSuiteRepository.save(testSuite);

//        gitlabService.runJob("1751", pipeline.getJobByName(service.getServiceName()).getId()); //stel assuming that pipeline creation returns the jobs too

        // Trigger async test simulation
        regressionSimulatorService.simulateTestRunAsync(testSuite.getId());
    }

    @Transactional
    public void updateTestSuiteStatus(UUID testSuiteId, TestSuiteStatus status) {
        TestSuite testSuite = testSuiteRepository.findById(testSuiteId)
                .orElseThrow(() -> new IllegalArgumentException("Test suite not found"));
        testSuite.setStatus(status);
        testSuiteRepository.save(testSuite);
    }

    public UUID getServiceIdByTestSuiteId(UUID testSuiteId) {
        TestSuite testSuite = testSuiteRepository.findById(testSuiteId)
                .orElseThrow(() -> new IllegalArgumentException("Test suite not found"));
        return testSuite.getService().getId();
    }

    @Transactional
    public void updateServiceStatus(UUID serviceId, ServiceStatus status) {
        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
        service.setStatus(status);
        serviceRepository.save(service);
    }

    public UUID getReleaseIdByServiceId(UUID serviceId) {
        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
        return service.getRelease().getId();
    }

    public void restartTestSuite(UUID id) {
        testSuiteRepository.deleteAllByTestSuiteId(id);
        regressionSimulatorService.simulateTestRun(id);
    }
}