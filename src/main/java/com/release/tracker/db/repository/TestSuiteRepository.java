package com.release.tracker.db.repository;

import com.release.tracker.db.entity.TestSuite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestSuiteRepository extends JpaRepository<TestSuite, UUID> {

    List<TestSuite> findByServiceId(UUID id);
}
