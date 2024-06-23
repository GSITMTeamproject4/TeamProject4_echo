package com.project.echoproject.controller;

import com.project.echoproject.entity.Coupon;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.CouponService;
import com.project.echoproject.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/mall")
@Controller
@RequiredArgsConstructor
public class MallController {
    private final SiteUserService siteUserService;
    private final CouponService couponService;

    @GetMapping("")
    public String mallHome(Model model) {
        List<Coupon> couponList = this.couponService.getList();
        model.addAttribute("couponList", couponList);
        model.addAttribute("coupon", new Coupon());
        return "mall";
    }

    @PostMapping("/buy")
    public String buyCoupon(@ModelAttribute Coupon coupon, Model model) { //selectCouponDTO 가 null
        SiteUser siteUser = this.siteUserService.findByUserId("test");
        model.addAttribute("coupon", coupon);
        // 디버깅을 위한 출력
        System.out.println("Received Coupon ID: " + coupon.getCouponIdHL());
        System.out.println("Received Coupon Name: " + coupon.getCouponName());
        System.out.println("Received Coupon Point: " + coupon.getCouponPoint());

        return "buy";
    }
}