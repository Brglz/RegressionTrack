package com.release.tracker.web.controller;

import com.release.tracker.db.entity.TestEntity;
import com.release.tracker.core.enums.TestStatus;
import com.release.tracker.core.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/api/tests")
public class TestController {
    @Autowired
    private TestService testService;

    @PostMapping("/{id}/status")
    public String updateTestStatus(@PathVariable UUID id, @RequestParam("status") String status) {
        testService.updateTestStatus(id, TestStatus.valueOf(status));
        TestEntity test = testService.getTestById(id);
        return "redirect:/test-suites/" + test.getTestSuite().getId() + "/tests";
    }
}