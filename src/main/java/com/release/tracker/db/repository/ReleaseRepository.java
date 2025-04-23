package com.release.tracker.db.repository;

import com.release.tracker.db.entity.Release;
import com.release.tracker.db.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReleaseRepository extends JpaRepository<Release, UUID> {

}
