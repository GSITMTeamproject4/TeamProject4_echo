package com.project.echoproject.service;

import com.project.echoproject.entity.Coupon;
import com.project.echoproject.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public List<Coupon> getList() {
        return couponRepository.findAll();
    }

    public Coupon getCoupon(Long id) {
        Optional<Coupon> coupon = couponRepository.findById(id);
        if(coupon.isPresent()) {
            return coupon.get();
        }else {
            throw new IllegalArgumentException("Coupon not found");
        }
    }
}
