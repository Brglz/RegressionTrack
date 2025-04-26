package com.release.tracker.web.controller;

import com.release.tracker.core.enums.TestStatus;
import com.release.tracker.core.service.ServiceService;
import com.release.tracker.core.service.TestService;
import com.release.tracker.db.dto.LastSuccessfulTestDto;
import com.release.tracker.db.entity.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private ServiceService serviceService;

    @PostMapping("/{id}/status")
    public String updateTestStatus(@PathVariable UUID id, @RequestParam("status") String status) {
        testService.updateTestStatus(id, TestStatus.valueOf(status));
        TestEntity test = testService.getTestById(id);
        return "redirect:/test-suites/" + test.getTestSuite().getId() + "/tests";
    }

    @PostMapping("/mark-flaky")
    public String markFlakyTests(@RequestParam UUID serviceId, @RequestParam UUID testSuiteId) {
        testService.markFlakyTests(serviceId);

        return "redirect:/test-suites/" + testSuiteId + "/tests";
    }

    @PostMapping("/check-last-successful-run")
    public String checkLastSuccessfulRun(@RequestParam String serviceName,
                                         @RequestParam String testName,
                                         @RequestParam String testId,
                                         Model model) {
        List<LastSuccessfulTestDto> successfulTests = testService.getLastSuccessfulTests(serviceName, testName);
        model.addAttribute("successfulTests", successfulTests);
        return "fragments/history-table-rows :: body";
    }



}