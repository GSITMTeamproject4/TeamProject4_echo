package com.project.echoproject.dto;

import com.project.echoproject.entity.LikeBoard;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CouponDTO {

    private Long couponId;
    private String couponName;
    private Long couponPoint;

    private Long currentPoint;
    private Long balance;
}
