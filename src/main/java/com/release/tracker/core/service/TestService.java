package com.release.tracker.core.service;

import com.release.tracker.core.enums.TestStatus;
import com.release.tracker.db.dto.LastSuccessfulTestDto;
import com.release.tracker.db.dto.LastSuccessfulTestView;
import com.release.tracker.db.entity.TestEntity;
import com.release.tracker.db.entity.TestSuite;
import com.release.tracker.db.repository.TestRepository;
import com.release.tracker.db.repository.TestSuiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestSuiteRepository testSuiteRepository;

    public TestEntity getTestById(UUID id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test not found: " + id));
    }

    public void updateTestStatus(UUID id, TestStatus status) {
        TestEntity test = getTestById(id);
        test.setStatus(status);
        testRepository.save(test);
    }

    public List<TestEntity> getTestsByTestSuiteId(UUID suiteId, String search, String status, String sort, int page, int size, boolean excludeFlaky) {
        List<TestEntity> tests = testRepository.findByTestSuiteId(suiteId);

        if (search != null && !search.isBlank()) {
            tests = tests.stream()
                    .filter(test -> test.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (status != null && !status.isBlank()) {
            tests = tests.stream()
                    .filter(test -> test.getStatus().name().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        if (excludeFlaky) {
            tests = tests.stream()
                    .filter(test -> !test.isFlaky())
                    .collect(Collectors.toList());
        }

        Comparator<TestEntity> comparator = Comparator.comparing(test -> switch (test.getStatus()) {
            case FAILED -> 0;
            case MITIGATED -> 1;
            case PASSED -> 2;
        });

        if ("desc".equalsIgnoreCase(sort)) {
            comparator = comparator.reversed();
        }

        tests.sort(comparator);

        int total = tests.size();
        if (total == 0) {
            return Collections.emptyList();
        }

        int totalPages = (int) Math.ceil((double) total / size);
        page = Math.max(0, Math.min(page, totalPages - 1));

        int start = page * size;
        int end = Math.min(start + size, total);

        return tests.subList(start, end);
    }

    public int getTotalTestsCount(UUID suiteId, String search, String status, boolean excludeFlaky) {
        List<TestEntity> tests = testRepository.findByTestSuiteId(suiteId);

        // Apply search filter
        if (search != null && !search.isBlank()) {
            tests = tests.stream()
                    .filter(test -> test.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Apply status filter
        if (status != null && !status.isBlank()) {
            tests = tests.stream()
                    .filter(test -> test.getStatus().name().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        if (excludeFlaky) {
            tests = tests.stream()
                    .filter(test -> !Boolean.TRUE.equals(test.isFlaky()))
                    .collect(Collectors.toList());
        }

        return tests.size();
    }

    public void markFlakyTests(UUID serviceId) {
        List<TestSuite> suites = testSuiteRepository.findByServiceId(serviceId);
        if (suites.size() < 2) return;

        // Sort descending by startDate
        suites.sort(Comparator.comparing(TestSuite::getStartDate).reversed());
        TestSuite latest = suites.get(0);
        List<TestEntity> latestTests = latest.getTests();

        // Flatten all previous passed test names
        Set<String> previouslyPassed = suites.stream()
                .skip(1)
                .flatMap(suite -> suite.getTests().stream())
                .filter(test -> test.getStatus().name().equals("PASSED"))
                .map(TestEntity::getName)
                .collect(Collectors.toSet());

        for (TestEntity test : latestTests) {
            if (test.getStatus().name().equals("FAILED") && previouslyPassed.contains(test.getName())) {
                test.setFlaky(true); // Add a flaky field to your TestEntity
            }
        }

        testSuiteRepository.save(latest); // Assuming cascade saves the tests
    }

    public List<LastSuccessfulTestDto> getLastSuccessfulTests(String serviceName, String testName) {
        List<LastSuccessfulTestView> rawResults =
                testRepository.findLast3SuccessfulTestsNative(serviceName, testName);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return rawResults.stream()
                .map(view -> new LastSuccessfulTestDto(
                        view.getReleaseName(),
                        view.getTestSuiteName(),
                        view.getStartDate().format(formatter)))
                .collect(Collectors.toList());
    }

}