package com.release.tracker.db.entity;

import com.release.tracker.core.enums.TestSuiteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "test_suite")
@Getter
@Setter
public class TestSuite {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "service_id", columnDefinition = "CHAR(36)")
    private ServiceEntity service;

    private String name;

    @Enumerated(EnumType.STRING)
    private TestSuiteStatus status;

    @OneToMany(mappedBy = "testSuite")
    private List<TestEntity> tests;

    @Column(name = "start_date")
    private LocalDateTime startDate;
}