package com.release.tracker.db.repository;

import com.release.tracker.db.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestRepository extends JpaRepository<TestEntity, UUID> {
}
