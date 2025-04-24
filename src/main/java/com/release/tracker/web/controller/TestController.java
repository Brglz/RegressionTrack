package com.release.tracker.web.controller;

import com.release.tracker.core.enums.TestStatus;
import com.release.tracker.core.service.ServiceService;
import com.release.tracker.core.service.TestService;
import com.release.tracker.db.dto.LastSuccessfulTestDto;
import com.release.tracker.db.entity.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
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

//    @PostMapping("/check-last-successful-run")
//    public ModelAndView checkLastSuccessfulRun(@RequestParam String serviceName,
//                                               @RequestParam String testName,
//                                               @RequestParam String testId) {
//        List<LastSuccessfulTestDto> successfulTests = testService.getLastSuccessfulTests(serviceName, testName);
//
//        // Create a ModelAndView object and set the view name and model attributes
//        ModelAndView modelAndView = new ModelAndView(?????);  // This is the fragment view name
//        modelAndView.addObject("successfulTests", successfulTests);  // Add the data to the model
//        modelAndView.addObject("testId", testId);  // Add the test ID to the model
//
//        return modelAndView;  // Return the ModelAndView to be rendered
//    }

    @PostMapping("/check-last-successful-run")
    @ResponseBody
    public String checkLastSuccessfulRun(@RequestParam String serviceName,
                                         @RequestParam String testName,
                                         @RequestParam String testId) {

        List<LastSuccessfulTestDto> successfulTests = testService.getLastSuccessfulTests(serviceName, testName);

        if (successfulTests.isEmpty()) {
            return "<tr><td colspan='3' class='text-muted'>No successful test runs found.</td></tr>";
        }

        StringBuilder sb = new StringBuilder();
        for (LastSuccessfulTestDto dto : successfulTests) {
            sb.append("<tr>")
                    .append("<td>").append(dto.getReleaseName()).append("</td>")
                    .append("<td>").append(dto.getTestSuiteName()).append("</td>")
                    .append("<td>").append(dto.getLastSuccessDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("</td>")
                    .append("</tr>");
        }

        return sb.toString();
    }


}