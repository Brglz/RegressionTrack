package com.release.tracker.db.repository;

import com.release.tracker.db.dto.LastSuccessfulTestDto;
import com.release.tracker.db.dto.LastSuccessfulTestView;
import com.release.tracker.db.entity.TestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TestRepository extends JpaRepository<TestEntity, UUID> {

    List<TestEntity> findByTestSuiteId(UUID suiteId);

    @Query(value = """
        SELECT r.name AS releaseName, ts.name AS testSuiteName, ts.start_date AS startDate
        FROM test t
        JOIN test_suite ts ON t.test_suite_id = ts.id
        JOIN service s ON ts.service_id = s.id
        JOIN release r ON s.release_id = r.id
        WHERE s.service_name = :serviceName
          AND t.name = :testName
          AND t.status = 'PASSED'
        ORDER BY ts.start_date DESC
        LIMIT 3
        """, nativeQuery = true)
    List<LastSuccessfulTestView> findLast3SuccessfulTestsNative(
            @Param("serviceName") String serviceName,
            @Param("testName") String testName
    );



//    @Query("SELECT new com.release.tracker.db.dto.LastSuccessfulTestDto(r.name, ts.name, ts.startDate) " +
//            "FROM TestEntity t " +
//            "JOIN TestSuite ts ON t.testSuite.id = ts.id " +
//            "JOIN ServiceEntity s ON ts.service.id = s.id " +
//            "JOIN Release r ON s.release.id = r.id " +
//            "WHERE s.serviceName = :serviceName " +
//            "AND t.name = :testName " +
//            "AND t.status = 'PASSED' " +
//            "ORDER BY ts.startDate DESC")
//    List<LastSuccessfulTestDto> findTop3SuccessfulTests(@Param("serviceName") String serviceName,
//                                                        @Param("testName") String testName);


}
