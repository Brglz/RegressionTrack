package com.release.tracker.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "release")
@Getter
@Setter
public class Release {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    private String name;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "release")
    private List<ServiceEntity> services;
}