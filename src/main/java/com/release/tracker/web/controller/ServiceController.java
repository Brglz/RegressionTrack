package com.release.tracker.web.controller;

import com.release.tracker.core.enums.ServiceStatus;
import com.release.tracker.core.enums.TestSuiteStatus;
import com.release.tracker.db.entity.ServiceEntity;
import com.release.tracker.db.entity.TestSuite;
import com.release.tracker.core.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @GetMapping("services/{id}/test-suites")
    public ModelAndView getTestSuitesByService(@PathVariable UUID id) {
        List<TestSuite> testSuites = serviceService.getTestSuitesByService(id);
        ServiceEntity service = serviceService.getServiceById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("testSuites", testSuites);
        modelAndView.addObject("service", service);
        modelAndView.setViewName("test-suite");

        return modelAndView;
    }

    @PostMapping("services/create-for-release/{id}")
    public String createServiceForRelease(@PathVariable UUID id, @RequestParam String serviceName,
                                          @RequestParam String version, @RequestParam String status) {
        serviceService.createServiceForRelease(id, serviceName, version, status);
        return "redirect:/releases/" + id + "/services";
    }

    @GetMapping("/services/{id}/run-regression")
    public String runRegression(@PathVariable UUID id) {
        serviceService.runRegressionTestSuite(id);
        return "redirect:/services/" + id + "/test-suites";
    }

    @PostMapping("/api/test-suites/{id}/status")
    public String updateTestSuiteStatus(@PathVariable UUID id, @RequestParam String status) {
        serviceService.updateTestSuiteStatus(id, TestSuiteStatus.valueOf(status));
        return "redirect:/services/" + serviceService.getServiceIdByTestSuiteId(id) + "/test-suites";
    }

    @PostMapping("/api/services/{id}/status")
    public String updateServiceStatus(@PathVariable UUID id, @RequestParam String status) {
        serviceService.updateServiceStatus(id, ServiceStatus.valueOf(status));
        return "redirect:/releases/" + serviceService.getReleaseIdByServiceId(id) + "/services";
    }

}