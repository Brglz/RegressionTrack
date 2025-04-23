package com.release.tracker.core.service;

import com.release.tracker.db.entity.Release;
import com.release.tracker.db.entity.ServiceEntity;
import com.release.tracker.db.repository.ReleaseRepository;
import com.release.tracker.db.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReleaseService {

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public Page<Release> getAllReleases(String search, String sort, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                "asc".equalsIgnoreCase(sort) ? Sort.by("releaseDate").ascending() : Sort.by("releaseDate").descending());

        if (search != null && !search.isBlank()) {
            return releaseRepository.findByNameContainingIgnoreCase(search, pageable);
        }

        return releaseRepository.findAll(pageable);
    }

    public List<ServiceEntity> getServicesByRelease(UUID releaseId) {
        return releaseRepository.findById(releaseId)
                .map(Release::getServices)
                .orElse(List.of());
    }

    public Release getReleaseById(UUID id) {
        return releaseRepository.findById(id).orElseThrow();
    }

    public void createRelease(String name, String releaseDate, List<UUID> serviceIds) {
        Release release = new Release();
        release.setId(UUID.randomUUID());
        release.setName(name);
        release.setReleaseDate(LocalDate.parse(releaseDate));
        if (serviceIds != null) {
            List<ServiceEntity> services = serviceRepository.findAllById(serviceIds);
            services.forEach(service -> service.setRelease(release));
            release.setServices(services);
        }
        releaseRepository.save(release);
    }

    public void addServicesToRelease(UUID releaseId, List<UUID> serviceIds) {
        if (serviceIds != null) {
            Release release = releaseRepository.findById(releaseId)
                    .orElseThrow(() -> new IllegalArgumentException("Release not found"));
            List<ServiceEntity> services = serviceRepository.findAllById(serviceIds);
            services.forEach(service -> service.setRelease(release));
            release.getServices().addAll(services);
            releaseRepository.save(release);
        }
    }
}
