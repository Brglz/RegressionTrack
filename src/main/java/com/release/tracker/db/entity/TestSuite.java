package com.release.tracker.db.entity;

import com.release.tracker.core.enums.TestSuiteStatus;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "test_suite")
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

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public ServiceEntity getService() { return service; }
    public void setService(ServiceEntity service) { this.service = service; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public TestSuiteStatus getStatus() { return status; }
    public void setStatus(TestSuiteStatus status) { this.status = status; }
    public List<TestEntity> getTests() { return tests; }
    public void setTests(List<TestEntity> tests) { this.tests = tests; }
}