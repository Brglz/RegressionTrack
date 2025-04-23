package com.release.tracker.web.controller;

import com.release.tracker.core.service.ReleaseService;
import com.release.tracker.core.service.ServiceService;
import com.release.tracker.db.entity.Release;
import com.release.tracker.db.entity.ServiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/releases")
public class ReleaseController {
    @Autowired
    private ReleaseService releaseService;

    @Autowired
    private ServiceService serviceService;

    @PostMapping("/create")
    public String createRelease(@RequestParam String name, @RequestParam String releaseDate,
                                @RequestParam(required = false) List<UUID> serviceIds) {
        releaseService.createRelease(name, releaseDate,serviceIds);
        return "redirect:/releases?search=&sort=desc";
    }

    @GetMapping("/{id}/services")
    public ModelAndView getServicesByRelease(@PathVariable UUID id, @RequestParam(defaultValue = "asc") String sort) {
        List<ServiceEntity> services = releaseService.getServicesByRelease(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("services", services);
        modelAndView.addObject("releaseId", id);
        modelAndView.setViewName("service");

        return modelAndView;
    }

    @PostMapping("/{id}/add-services")
    public String addServicesToRelease(@PathVariable UUID id, @RequestParam(required = false) List<UUID> serviceIds) {
        releaseService.addServicesToRelease(id, serviceIds);
        return "redirect:/releases/" + id + "/services";
    }

    @GetMapping
    public ModelAndView getAllReleases(@RequestParam(required = false) String search,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        Page<Release> releasesPage = releaseService.getAllReleases(search, sort, page, size);

        ModelAndView modelAndView = new ModelAndView("releases");
        modelAndView.addObject("releases", releasesPage.getContent());
        modelAndView.addObject("search", search);
        modelAndView.addObject("sort", sort);
        modelAndView.addObject("page", releasesPage);
        return modelAndView;
    }

}