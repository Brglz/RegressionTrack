package com.release.tracker.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastSuccessfulTestDto {
    private String releaseName;
    private String testSuiteName;
    private String lastSuccessDate;
}
