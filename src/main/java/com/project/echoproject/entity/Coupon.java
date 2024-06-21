package com.project.echoproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponIdHL; // SiteUser에 컬럼 해결
    @Column(length = 50, nullable = false)
    private Long couponPoint;
    @Column(length = 50, nullable = false)
    private String couponName;
}
