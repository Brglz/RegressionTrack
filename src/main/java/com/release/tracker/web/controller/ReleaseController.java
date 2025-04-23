package com.release.tracker.web.controller;

import com.release.tracker.core.service.ReleaseService;
import com.release.tracker.core.service.ServiceService;
import com.release.tracker.db.entity.Release;
import com.release.tracker.db.entity.ServiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
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
        return "redirect:/releases";
    }

    @GetMapping
    public ModelAndView getAllReleases() {
        List<Release> releases = releaseService.getAllReleases();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("releases", releases);
        modelAndView.setViewName("releases");

        return modelAndView;
    }

    @GetMapping("/{id}/services")
    public ModelAndView getServicesByRelease(@PathVariable UUID id) {
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

}