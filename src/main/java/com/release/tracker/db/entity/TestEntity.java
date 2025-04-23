package com.release.tracker.db.entity;

import com.release.tracker.core.enums.TestStatus;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "test")
public class TestEntity {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "test_suite_id", columnDefinition = "CHAR(36)")
    private TestSuite testSuite;

    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private TestStatus status;

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public TestSuite getTestSuite() { return testSuite; }
    public void setTestSuite(TestSuite testSuite) { this.testSuite = testSuite; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TestStatus getStatus() { return status; }
    public void setStatus(TestStatus status) { this.status = status; }
}