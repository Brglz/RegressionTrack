package com.release.tracker.db.dto;

import java.time.LocalDateTime;

public interface LastSuccessfulTestView {
    String getReleaseName();
    String getTestSuiteName();
    LocalDateTime getStartDate();
}

