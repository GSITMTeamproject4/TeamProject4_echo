package com.project.echoproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(length = 50, nullable = false)
    private Long couponPoint;

    @Column(length = 50, nullable = false)
    private String couponName;

    private String imgName;

}
