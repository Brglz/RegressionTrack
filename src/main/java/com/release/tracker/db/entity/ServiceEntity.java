package com.release.tracker.db.entity;

import com.release.tracker.core.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "service")
@Getter
@Setter
public class ServiceEntity {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "release_id", columnDefinition = "CHAR(36)")
    private Release release;

    @Column(name = "service_name")
    private String serviceName;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @OneToMany(mappedBy = "service")
    private List<TestSuite> testSuites;

    private String version;

}
