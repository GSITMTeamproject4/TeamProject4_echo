package com.project.echoproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    Coupon couponId;
}