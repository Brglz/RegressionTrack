package com.release.tracker.db.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LastSuccessfulTestDto {
    private String releaseName;
    private String testSuiteName;
    private LocalDateTime lastSuccessDate;
}
