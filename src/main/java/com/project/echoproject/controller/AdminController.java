package com.project.echoproject.controller;

import com.project.echoproject.entity.Challenge;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.exception.NoChallengeFoundException;
import com.project.echoproject.service.ChallengeService;
import com.project.echoproject.service.SiteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    private SiteUserService siteUserService;
    @Autowired
    private ChallengeService challengeService;

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

    @GetMapping("pointManage")
    public String adminPointManage(Model model) {
        try {
            List<Challenge> challenges = challengeService.getChallengAll();

            model.addAttribute("challenges", challenges);
            return "admin/pointManage"; // Assuming "challenge-list.html" is your template
        } catch (NoChallengeFoundException e) {
            model.addAttribute("errorMessage", "확인할 챌린지가 없습니다.");
            return "admin/noChallenge"; // Assuming "error-page.html" is your error template
        }
    }
//    @PostMapping("pointManage")
//    public String adminDeleteChallenge(Model model, @RequestParam("id") Long id) {
//        challengeService.deleteChallenge(id);
//        return "redirect:/admin/pointManage";
//    }
@PostMapping("pointManage")
public String adminAddPoint(Model model, @RequestParam("id") Long id, @RequestParam("userId") String userId){
    SiteUser siteUser = siteUserService.findByUserId(userId);
    siteUserService.addPointByAdmin(userId,500L);
    return "redirect:/admin/pointManage";
}
}
