package com.project.echoproject.service;

import com.project.echoproject.entity.ReportBoard;
import org.springframework.ui.Model;

import java.util.List;

public interface ReportBoardService {
    void reportBoard(Long listId, String userId, String reportReason, String reportContent);
    List<ReportBoard> getAllReports();

}
