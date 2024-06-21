package com.project.echoproject.controller;

import com.project.echoproject.dto.BuyDTO;
import com.project.echoproject.dto.selectCouponDTO;
import com.project.echoproject.entity.Coupon;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UserCoupon;
import com.project.echoproject.service.CouponService;
import com.project.echoproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequestMapping("/mall")
@Controller
@RequiredArgsConstructor
public class MallController {
    private final UserService userService;
    private final CouponService couponService;

    @GetMapping("")
    public String mallHome(Model model) {
        List<Coupon> couponList = this.couponService.getList();
        model.addAttribute("couponList", couponList);
        return "mall";
    }

    @PostMapping("/buy")
    public String buyCoupon(Coupon selectCouponDTO, Model model ) { //selectCouponDTO 가 null
        SiteUser siteUser = this.userService.getUser();

//        UserCoupon userCoupon = new UserCoupon();
//        userCoupon.setSiteUser(siteUser);
//        userCoupon.setCoupon(selectCouponDTO);
//        userCoupon.setUseYN(false);
//
//        model.addAttribute("buyDTO", userCoupon);
        return "buy";
    }
}
