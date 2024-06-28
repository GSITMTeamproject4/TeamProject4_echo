package com.project.echoproject.controller;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UserCoupon;
import com.project.echoproject.service.SiteUserService;
import com.project.echoproject.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;
import java.util.List;

@Controller
public class UserCouponController {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private SiteUserService siteUserService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/coupons")
    public String getUserCoupons(Model model, Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SiteUser user = siteUserService.findByUserId(auth.getName()); // 로그인한 유저 정보 가져오기

        List<UserCoupon> userCoupons = userCouponService.getUserCoupons(user);
        model.addAttribute("userCoupons", userCoupons);

        return "userCoupons"; // 뷰 템플릿 이름
    }
}
