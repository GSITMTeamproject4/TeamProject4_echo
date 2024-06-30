package com.project.echoproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userCouponId;

    @ManyToOne
    @JoinColumn(name = "userId")
    SiteUser userId;

    @ManyToOne
    @JoinColumn(name = "couponId")
    Coupon coupon;

    private LocalDateTime insertDate;
}




