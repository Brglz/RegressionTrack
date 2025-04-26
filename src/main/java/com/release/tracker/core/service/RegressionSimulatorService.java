package com.release.tracker.core.service;

import com.release.tracker.core.enums.TestStatus;
import com.release.tracker.core.enums.TestSuiteStatus;
import com.release.tracker.db.entity.TestEntity;
import com.release.tracker.db.entity.TestSuite;
import com.release.tracker.db.repository.TestRepository;
import com.release.tracker.db.repository.TestSuiteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.release.tracker.core.enums.TestStatus.FAILED;
import static com.release.tracker.core.enums.TestStatus.PASSED;
import static com.release.tracker.core.enums.TestSuiteStatus.COMPLETED;

@Service
public class RegressionSimulatorService {

    private static final List<String> ERROR_MESSAGES = Arrays.asList(
            "NullPointerException in module X",
            "Database connection timeout",
            "Assertion failed in test case Y",
            "IOException during file read",
            "Unexpected API response"
    );

    private final TestSuiteRepository testSuiteRepository;
    private final TestRepository testRepository;

    @Autowired
    public RegressionSimulatorService(TestSuiteRepository testSuiteRepository, TestRepository testRepository) {
        this.testSuiteRepository = testSuiteRepository;
        this.testRepository = testRepository;
    }

    @Async
    @Transactional
    public void simulateTestRunAsync(UUID testSuiteId) {
        simulateTestRun(testSuiteId);
    }

    public void simulateTestRun(UUID testSuiteId) {
        try {
            Thread.sleep(5000); // 5-second delay
            TestSuite testSuite = testSuiteRepository.findById(testSuiteId)
                    .orElseThrow(() -> new IllegalArgumentException("Test suite not found"));

            Random random = new Random();
            TestStatus[] statuses = {PASSED, FAILED};
            boolean hasFailed = false;

            for (int i = 1; i <= 30; i++) {
                TestEntity test = new TestEntity();
                test.setId(UUID.randomUUID());
                test.setName("Test_" + i);
                TestStatus status = statuses[random.nextInt(statuses.length)];
                test.setStatus(status);
                if (status == FAILED) {
                    hasFailed = true;
                    test.setDescription(ERROR_MESSAGES.get(random.nextInt(ERROR_MESSAGES.size())));
                }
                test.setTestSuite(testSuite);
                testRepository.save(test);
            }

            testSuite.setStatus(hasFailed ? TestSuiteStatus.FAILED : COMPLETED);
            testSuiteRepository.save(testSuite);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
