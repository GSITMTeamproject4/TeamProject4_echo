package com.project.echoproject.dto;

import com.project.echoproject.entity.ReportBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReportDTO {
//    private Long reportId;
//    private Long authBoardId;
//    private String reportReason;
//    private String reportContent;
//    private String reportedUserId;
    ReportBoard reportBoard;
    private String writeId;
}
