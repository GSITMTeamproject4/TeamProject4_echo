package com.project.echoproject.service;

import org.springframework.ui.Model;

public interface ReportBoardService {
    void reportBoard(Long listId, String userId, String reportReason, String reportContent);

}
