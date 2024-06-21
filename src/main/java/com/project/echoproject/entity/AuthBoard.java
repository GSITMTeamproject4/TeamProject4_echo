package com.project.echoproject.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class AuthBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listId;

    @Column(length = 200, nullable = false)
    private String checkImg;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int viewCount;

    private LocalDateTime postCreateDate;
    private LocalDateTime postModifyDate;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private SiteUser siteUser;

    @PrePersist
    protected void onCreate() {
        this.postCreateDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.postModifyDate = LocalDateTime.now();
    }
}
