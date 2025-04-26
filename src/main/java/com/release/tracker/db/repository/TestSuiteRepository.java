package com.release.tracker.db.repository;

import com.release.tracker.db.entity.TestSuite;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestSuiteRepository extends JpaRepository<TestSuite, UUID> {

    List<TestSuite> findByServiceId(UUID id);

    @Transactional
    @Modifying
    @Query("DELETE FROM TestEntity t WHERE t.testSuite.id = :testSuiteId")
    void deleteAllByTestSuiteId(@Param("testSuiteId") UUID testSuiteId);
}
