package com.project.echoproject.repository;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCoupon,Long> {
    List<UserCoupon> findByUserId(SiteUser userId);
}