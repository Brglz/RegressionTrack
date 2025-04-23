package com.release.tracker.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @OneToMany(mappedBy = "release")
    private List<ServiceEntity> services;
}