package com.project.echoproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @Column(nullable = false)
    private String userId;

    // 필수 입력 부분
    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private String nickName;

    // 선택 입력 부분
    private String phoneNum;

    private String gender;

    private String zipcode;

    private String streetaddr;

    private String detailaddr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private Image profileImage;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private Integer reportCnt = 0;

    private boolean couponUse = false;

    // 소셜 로그인
    private String provider;  // "google", "naver", "kakao", "local"

    private String providerId;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;


    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<LikeBoard> likeBoards;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<UseAmount> useAmounts;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.REMOVE)
    private List<UserCoupon> userCoupons;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<Point> points;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<AuthBoard> authBoards;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<Challenge> challenges;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifyDate = LocalDateTime.now();
    }

    private Long currentPoint = 0L;
}