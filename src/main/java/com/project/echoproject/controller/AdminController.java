package com.project.echoproject.controller;

import com.project.echoproject.dto.ReportDTO;
import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.AuthBoardServiceImpl;
import com.project.echoproject.service.ReportBoardService;
import com.project.echoproject.service.SiteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    private SiteUserService siteUserService;
    @Autowired
    private ReportBoardService reportBoardService;
    @Autowired
    private AuthBoardServiceImpl authBoardServiceImpl;

    @GetMapping("")
    public String admin() {
        return "admin/admin"; // /templates/admin/admin.html 을 의미합니다.
    }

    @GetMapping("/memberList")
    public String adminMemberList(Model model) {
        List<SiteUser> users = siteUserService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/memberList"; // /templates/admin/adminMemberList.html 을 의미합니다.
    }

    @GetMapping("/challengeList")
    public String adminChallengeList() {
        return "admin/challengeList";
    }

    @GetMapping("/reportList")
    public String adminReportList(Model model) {
        List<ReportDTO> reports = reportBoardService.getAllReports(); // 신고 목록을 가져옴
        model.addAttribute("reports", reports);
        return "admin/reportList"; // /templates/admin/reportList.html 을 의미합니다.
    }

    @GetMapping("/reportDetail/{id}")
    public String adminReportDetail(@PathVariable("id") Long authBoardId, Model model) {
        AuthBoard authBoard = authBoardServiceImpl.getAuthBoard(authBoardId); // 해당 게시글을 가져옴
        model.addAttribute("board", authBoard);
        return "/authBoard/authBoard_detail"; // 신고된 게시글의 상세 페이지로 이동합니다.
    }

}
