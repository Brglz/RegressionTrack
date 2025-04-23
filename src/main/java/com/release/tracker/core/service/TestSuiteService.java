package com.release.tracker.core.service;

import com.release.tracker.core.enums.TestSuiteStatus;
import com.release.tracker.db.entity.TestEntity;
import com.release.tracker.db.entity.TestSuite;
import com.release.tracker.db.repository.TestSuiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TestSuiteService {
    @Autowired
    private TestSuiteRepository testSuiteRepository;

    public List<TestEntity> getTestsByTestSuite(UUID testSuiteId) {
        return testSuiteRepository.findById(testSuiteId)
                .map(TestSuite::getTests)
                .orElse(List.of());
    }

    public TestSuite getTestSuiteById(UUID id) {
        return testSuiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test Suite not found: " + id));
    }

    public void updateTestSuiteStatus(UUID id, TestSuiteStatus status) {
        TestSuite testSuite = getTestSuiteById(id);
        testSuite.setStatus(status);
        testSuiteRepository.save(testSuite);
    }
}