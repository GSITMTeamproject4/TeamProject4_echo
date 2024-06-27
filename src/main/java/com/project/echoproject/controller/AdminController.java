package com.project.echoproject.controller;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.SiteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    private SiteUserService siteUserService;

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
}
