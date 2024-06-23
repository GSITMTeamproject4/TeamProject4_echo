package com.project.echoproject.controller;

import com.project.echoproject.dto.CouponDTO;
import com.project.echoproject.entity.Coupon;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.CouponService;
import com.project.echoproject.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/mall")
@Controller
@RequiredArgsConstructor
public class MallController {
    private final CouponService couponService;
    private final SiteUserService siteUserService;

    @GetMapping("")
    public String mallHome(Model model) {
        List<Coupon> couponList = this.couponService.getList();
        model.addAttribute("couponList", couponList);
        return "mall";
    }

    @GetMapping("/buy/{id}")
    public String buyCoupon(@PathVariable Long id, Model model,Principal principal ) {
        Coupon coupon = couponService.getCoupon(id);
        model.addAttribute("coupon", coupon);

        SiteUser siteUser = siteUserService.findByUserId(principal.getName());

        CouponDTO buyCoupon = new CouponDTO();
        buyCoupon.setCouponPoint(coupon.getCouponPoint());
        buyCoupon.setCouponName(coupon.getCouponName());
        buyCoupon.setCurrentPoint(siteUser.getCurrentPoint());
        buyCoupon.setBalance(0L);

        model.addAttribute("buyCoupon",buyCoupon);
        return "buy";
    }
}
