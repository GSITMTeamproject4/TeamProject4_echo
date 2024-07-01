package com.project.echoproject.controller;

import com.project.echoproject.dto.ReportDTO;
import com.project.echoproject.entity.*;
import com.project.echoproject.exception.NoChallengeFoundException;
import com.project.echoproject.repository.ProductRepository;
import com.project.echoproject.repository.ReportBoardRepository;
import com.project.echoproject.service.*;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {
//커밋
    @Autowired
    private SiteUserService siteUserService;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private ReportBoardService reportBoardService;

    @Autowired
    private AuthBoardService authBoardService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

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

    @GetMapping("point")
    public String adminPointManage(Model model) {
        try {
            List<Challenge> challenges = challengeService.getChallengAll();
            model.addAttribute("challenges", challenges);
            return "admin/pointManage";
        } catch (NoChallengeFoundException e) {
            model.addAttribute("errorMessage", "확인할 챌린지가 없습니다.");
            return "admin/noChallenge";
        }
    }

    @PostMapping("point")
    public String adminAddPoint(Model model,
                                @RequestParam("id") Long id,
                                @RequestParam("userId") String userId,
                                @RequestParam("challengeInfo") String challengeInfo){
        SiteUser siteUser = siteUserService.findByUserId(userId);
        siteUserService.addPointByAdmin(userId,500L,challengeInfo);
        return "redirect:/admin/point";
    }

    @GetMapping("/showChallenge/{id}")
    public String adminChallengeDetail(@PathVariable("id") Long challengeId, Model model) {
        Challenge challenge = challengeService.getChallengeById(challengeId);
        model.addAttribute("challenge", challenge);
        return "showChallenge"; // 신고된 게시글의 상세 페이지로 이동합니다.
    }

    @GetMapping("/report")
    public String adminReportListBoard(Model model) {

        List<ReportBoard> reports = reportBoardService.getAllReports();

        model.addAttribute("reports",reports);
        return "admin/report";
    }
    @PostMapping("/report")
    public String adminRemoveBoard(@RequestParam("id") Long id, @RequestParam("userId") String userId) {
        SiteUser siteUser = siteUserService.findByUserId(userId);
        authBoardService.deleteBoard(id,siteUser);
        return "redirect:/admin/report";
    }

    @GetMapping("/reportDetail/{id}")
    public String adminReportDetail(@PathVariable("id") Long authBoardId, Model model) {
        AuthBoard authBoard = authBoardService.getAuthBoard(authBoardId); // 해당 게시글을 가져옴
        model.addAttribute("board", authBoard);
        return "/authBoard/authBoard_detail"; // 신고된 게시글의 상세 페이지로 이동합니다.
    }

    @GetMapping("/login")
    public String login() {
        return "admin/loginFormAdmin";
    }

    @GetMapping("/")
    public String index() {
        return "admin";
    }

    @GetMapping("coupon")
    public String adminCouponManage() {
        return "admin/coupon";
    }

    @PostMapping("/coupon")
    public String createPost(@RequestParam("name") String name,
                             @RequestParam("point") Long point,
                             @RequestParam("file") MultipartFile file,
                             Principal principal,
                             Model model) {

        try {
            if (principal == null) {
                // 인증되지 않은 경우 접근 거부 예외 발생
                return "redirect:/access_denied";
            }

            couponService.addCoupon(name,point,file);
            return "redirect:/mall";
        } catch (IOException e) {
            model.addAttribute("error", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return "authBoard/authBoard_create";
        }
    }
    @GetMapping("/notice")
    public String notice() {
        return "admin/notice_list_admin";
    }

    @GetMapping("/item")
    public String item() {
        return "admin/item";
    }

    @PostMapping("/item")
    public String createItem(@RequestParam("name") String name,
                             @RequestParam("price") int price,
                             @RequestParam("file") MultipartFile file,
                             Principal principal,
                             Model model) {

        try {
            if (principal == null) {
                // 인증되지 않은 경우 접근 거부 예외 발생
                return "redirect:/access_denied";
            }

            productService.addItem(name,price,file);
            return "redirect:/product";
        } catch (IOException e) {
            model.addAttribute("error", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return "authBoard/authBoard_create";
        }
    }
}
