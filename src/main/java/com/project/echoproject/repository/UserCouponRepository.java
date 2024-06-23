package com.project.echoproject.repository;

import com.project.echoproject.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon,Long> {
}