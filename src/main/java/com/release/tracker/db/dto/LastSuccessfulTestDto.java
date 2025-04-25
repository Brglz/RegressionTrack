package com.release.tracker.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastSuccessfulTestDto {
    private String releaseName;
    private String testSuiteName;
    private String lastSuccessDate;
//    private LocalDateTime lastSuccessDate;
}
