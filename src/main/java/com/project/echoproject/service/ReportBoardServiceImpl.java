package com.project.echoproject.service;

import com.project.echoproject.DuplicateReportException;
import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.ReportBoard;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.AuthBoardRepository;
import com.project.echoproject.repository.ReportBoardRepository;
import com.project.echoproject.repository.SiteUserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportBoardServiceImpl implements ReportBoardService {

    private final ReportBoardRepository reportBoardRepository;
    private final AuthBoardRepository authBoardRepository;
    private final SiteUserRepository siteUserRepository;

    @Override
    public void reportBoard(Long listId, String userId, String reportReason, String reportContent) {
        AuthBoard authBoard = authBoardRepository.findById(listId).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        SiteUser siteUser = siteUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 자신의 글인지 확인
        if (authBoard.getSiteUser().getUserId().equals(userId)) {
            throw new RuntimeException("자신의 글은 신고할 수 없습니다.");
        }

        // 이미 신고했는지 확인
        boolean alreadyReported = reportBoardRepository.findByAuthBoardAndSiteUser(authBoard, siteUser).isPresent();
        if (alreadyReported) {
            throw new DuplicateReportException("이미 신고한 게시글입니다.");
        }

        // 신고 생성
        ReportBoard reportBoard = new ReportBoard();
        reportBoard.setAuthBoard(authBoard);
        reportBoard.setSiteUser(siteUser);
        reportBoard.setReportReason(reportReason);
        reportBoard.setReportContent(reportContent);
        reportBoardRepository.save(reportBoard);

        // 신고 당한 유저의 reportCnt 증가
        SiteUser reportedUser = authBoard.getSiteUser();
        reportedUser.setReportCnt(reportedUser.getReportCnt() + 1);
        siteUserRepository.save(reportedUser);
    }
}
