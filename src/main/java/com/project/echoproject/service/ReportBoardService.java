package com.project.echoproject.service;

import com.project.echoproject.DuplicateReportException;
import com.project.echoproject.dto.ReportDTO;
import com.project.echoproject.entity.ReportBoard;

import java.util.List;

public interface ReportBoardService {
    void reportBoard(Long listId, String userId, String reportReason, String reportContent) throws DuplicateReportException;

    List<ReportBoard> getAllReports();
}
