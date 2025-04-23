package com.release.tracker.web.controller;

import com.release.tracker.core.enums.TestSuiteStatus;
import com.release.tracker.core.service.TestSuiteService;
import com.release.tracker.db.entity.TestEntity;
import com.release.tracker.db.entity.TestSuite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/test-suites")
public class TestSuiteController {
    @Autowired
    private TestSuiteService testSuiteService;

    @GetMapping("/{id}/tests")
    public ModelAndView getTestsByTestSuite(@PathVariable UUID id) {
        List<TestEntity> tests = testSuiteService.getTestsByTestSuite(id);
        TestSuite testSuite = testSuiteService.getTestSuiteById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("tests", tests);
        modelAndView.addObject("testSuite", testSuite);
        modelAndView.setViewName("test");

        return modelAndView;
    }

    @PostMapping("/api/test-suites/{id}/status")
    public String updateTestSuiteStatus(@PathVariable UUID id, @RequestParam("status") String status) {
        testSuiteService.updateTestSuiteStatus(id, TestSuiteStatus.valueOf(status));
        TestSuite testSuite = testSuiteService.getTestSuiteById(id);
        return "redirect:/services/" + testSuite.getService().getId() + "/test-suites";
    }
}