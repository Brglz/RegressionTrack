package com.release.tracker.core.service;

import com.release.tracker.core.enums.TestStatus;
import com.release.tracker.db.entity.TestEntity;
import com.release.tracker.db.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    public TestEntity getTestById(UUID id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test not found: " + id));
    }

    public void updateTestStatus(UUID id, TestStatus status) {
        TestEntity test = getTestById(id);
        test.setStatus(status);
        testRepository.save(test);
    }

    public List<TestEntity> getTestsByTestSuiteId(UUID suiteId, String search, String status, String sort, int page, int size) {
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

        // Apply sorting
        Comparator<TestEntity> comparator = Comparator.comparing(test -> {
            switch (test.getStatus()) {
                case FAILED: return 0;
                case MITIGATED: return 1;
                case PASSED: return 2;
                default: return 3;
            }
        });

        // Apply sorting direction
        if ("desc".equalsIgnoreCase(sort)) {
            comparator = comparator.reversed();
        }

        // Sort the tests
        tests.sort(comparator);

        // Implement pagination
        int start = page * size;
        int end = Math.min(start + size, tests.size());

        return tests.subList(start, end); // Return only the page of results
    }

    public int getTotalTestsCount(UUID suiteId, String search, String status) {
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

        return tests.size();
    }
}