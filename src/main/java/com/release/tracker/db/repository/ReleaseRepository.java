package com.release.tracker.db.repository;

import com.release.tracker.db.entity.Release;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReleaseRepository extends JpaRepository<Release, UUID> {

    Page<Release> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
