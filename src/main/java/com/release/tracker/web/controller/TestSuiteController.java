package com.release.tracker.web.controller;

import com.release.tracker.core.enums.TestSuiteStatus;
import com.release.tracker.core.service.TestService;
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

    @Autowired
    private TestService testService;

    @PostMapping("/api/test-suites/{id}/status")
    public String updateTestSuiteStatus(@PathVariable UUID id, @RequestParam("status") String status) {
        testSuiteService.updateTestSuiteStatus(id, TestSuiteStatus.valueOf(status));
        TestSuite testSuite = testSuiteService.getTestSuiteById(id);
        return "redirect:/services/" + testSuite.getService().getId() + "/test-suites";
    }

    @GetMapping("/{id}/tests")
    public ModelAndView getTests(@PathVariable UUID id,
                                 @RequestParam(required = false) String search,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false, defaultValue = "asc") String sort) {
        ModelAndView mav = new ModelAndView("test");

        TestSuite testSuite = testSuiteService.getTestSuiteById(id);
        List<TestEntity> filteredTests = testService.getTestsByTestSuiteId(id, search, status, sort);

        mav.addObject("testSuite", testSuite);
        mav.addObject("tests", filteredTests);
        mav.addObject("search", search);
        mav.addObject("selectedStatus", status);
        mav.addObject("sortDirection", sort);

        return mav;
    }
}