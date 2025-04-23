package com.release.tracker.core.service;

import com.release.tracker.db.entity.Release;
import com.release.tracker.db.entity.ServiceEntity;
import com.release.tracker.db.repository.ReleaseRepository;
import com.release.tracker.db.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Release> getAllReleases() {
        return releaseRepository.findAll();
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
