package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UserCoupon;
import com.project.echoproject.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;

    public void addCoupon(UserCoupon coupon) {
        userCouponRepository.save(coupon);
    }

    public List<UserCoupon> getUserCoupons(SiteUser user) {
        return userCouponRepository.findByUserId(user);
    }
}
