package com.release.tracker.db.repository;

import com.release.tracker.db.dto.LastSuccessfulTestDto;
import com.release.tracker.db.entity.TestEntity;
import com.release.tracker.db.entity.TestSuite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestRepository extends JpaRepository<TestEntity, UUID> {

    List<TestEntity> findByTestSuiteId(UUID suiteId);

    @Query("SELECT new com.example.dto.TestSuccessDTO(r.name, ts.name, ts.startDate) " +
            "FROM Test t " +
            "JOIN TestSuite ts ON t.testSuite.id = ts.id " +
            "JOIN Service s ON ts.service.id = s.id " +
            "JOIN Release r ON s.release.id = r.id " +
            "WHERE s.serviceName = :serviceName " +
            "AND t.name = :testName " +
            "AND t.status = 'PASSED' " +
            "ORDER BY ts.startDate DESC")
    Page<LastSuccessfulTestDto> findLast3SuccessfulTests(@Param("serviceName") String serviceName,
                                                         @Param("testName") String testName, Pageable pageable);

}
