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
        return this.couponRepository.findAll();
    }
}
