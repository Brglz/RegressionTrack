package com.release.tracker.core.service;

import com.release.tracker.core.enums.ServiceStatus;
import com.release.tracker.core.enums.TestSuiteStatus;
import com.release.tracker.db.entity.Release;
import com.release.tracker.db.entity.TestSuite;
import com.release.tracker.db.entity.ServiceEntity;
import com.release.tracker.db.repository.ReleaseRepository;
import com.release.tracker.db.repository.ServiceRepository;
import com.release.tracker.db.repository.TestRepository;
import com.release.tracker.db.repository.TestSuiteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceService {
    private final ServiceRepository serviceRepository;

    private final ReleaseRepository releaseRepository;

    private final TestSuiteRepository testSuiteRepository;

    private final RegressionSimulatorService regressionSimulatorService;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, ReleaseRepository releaseRepository,
                          TestSuiteRepository testSuiteRepository, RegressionSimulatorService regressionSimulatorService) {
        this.serviceRepository = serviceRepository;
        this.releaseRepository = releaseRepository;
        this.testSuiteRepository = testSuiteRepository;
        this.regressionSimulatorService = regressionSimulatorService;
    }

    public List<TestSuite> getTestSuitesByService(UUID serviceId) {
        return serviceRepository.findById(serviceId)
                .map(ServiceEntity::getTestSuites)
                .orElse(List.of());
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

        TestSuite testSuite = new TestSuite();
        testSuite.setId(UUID.randomUUID());
        testSuite.setName("Regression_" + LocalDate.now());
        testSuite.setService(service);
        testSuite.setStatus(TestSuiteStatus.IN_PROGRESS);
        testSuiteRepository.save(testSuite);

        // Trigger async test simulation
        regressionSimulatorService.simulateTestRun(testSuite.getId());
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
}