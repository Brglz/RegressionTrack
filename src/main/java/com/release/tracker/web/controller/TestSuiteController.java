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
    public ModelAndView getTestsByTestSuiteFilteredAndSorted(
            @PathVariable UUID id,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "asc") String sort,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {

        List<TestEntity> tests = testService.getTestsByTestSuiteId(id, search, status, sort, page, size);
        int totalTests = testService.getTotalTestsCount(id, search, status);

        ModelAndView modelAndView = new ModelAndView("test");
        modelAndView.addObject("tests", tests);
        modelAndView.addObject("testSuite", testSuiteService.getTestSuiteById(id));
        modelAndView.addObject("search", search);
        modelAndView.addObject("status", status);
        modelAndView.addObject("sort", sort);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", (int) Math.ceil((double) totalTests / size));

        return modelAndView;
    }

}