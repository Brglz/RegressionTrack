package com.release.tracker.core.service;

import com.release.tracker.core.enums.TestStatus;
import com.release.tracker.db.entity.TestEntity;
import com.release.tracker.db.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
}