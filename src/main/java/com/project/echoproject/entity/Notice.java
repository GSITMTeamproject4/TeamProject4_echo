package com.project.echoproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@Entity
public class Notice {

    @Id
    @Column(columnDefinition = "INT DEFAULT 0", nullable = true)
    private long notice_id;

    @Column(length = 50 , nullable = false)
    private String notice_title;

    @Column(length = 500 , nullable = false)
    private String notice_content;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifyDate = LocalDateTime.now();
    }

}
