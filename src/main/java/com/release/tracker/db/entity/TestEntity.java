package com.release.tracker.db.entity;

import com.release.tracker.core.enums.TestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "test")
@Getter
@Setter
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

    @Column(name = "is_flaky")
    private boolean isFlaky;

}