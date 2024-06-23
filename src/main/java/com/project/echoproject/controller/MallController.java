package com.project.echoproject.controller;

import com.project.echoproject.entity.Coupon;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.CouponService;
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

    @GetMapping("")
    public String mallHome(Model model) {
        List<Coupon> couponList = this.couponService.getList();
        model.addAttribute("couponList", couponList);
        return "mall";
    }

    @GetMapping("/buy/{id}")
    public String buyCoupon(@PathVariable Long id, Model model ) {

        Coupon coupon = couponService.getCoupon(id);
        model.addAttribute("coupon", coupon);
        return "buy";
    }
}
