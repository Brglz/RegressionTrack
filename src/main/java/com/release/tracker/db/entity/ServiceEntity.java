package com.release.tracker.db.entity;

import com.release.tracker.core.enums.ServiceStatus;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "service")
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

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Release getRelease() { return release; }
    public void setRelease(Release release) { this.release = release; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public ServiceStatus getStatus() { return status; }
    public void setStatus(ServiceStatus status) { this.status = status; }
    public List<TestSuite> getTestSuites() { return testSuites; }
    public void setTestSuites(List<TestSuite> testSuites) { this.testSuites = testSuites; }
}
