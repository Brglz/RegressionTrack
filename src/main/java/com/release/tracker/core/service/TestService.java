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

    public List<TestEntity> getTestsByTestSuiteId(UUID suiteId, String search, String status, String sort) {
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

        Comparator<TestEntity> comparator = Comparator.comparing(TestEntity::getStatus);
        if ("desc".equalsIgnoreCase(sort)) {
            comparator = comparator.reversed();
        }
        tests.sort(comparator);

        return tests;
    }


}