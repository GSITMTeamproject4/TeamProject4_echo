package com.project.echoproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@Entity
public class Notice {

    @Id
    @Column(columnDefinition = "INT DEFAULT 0", nullable = true)
    private int notice_id;

    @Column(length = 50 , nullable = false)
    private String notice_title;

    @Column(length = 500 , nullable = false)
    private String notice_content;


    @Column(nullable = false)
    private LocalDateTime notice_date;

    @Column(nullable = false)
    private LocalDateTime notice_modify_date;




}
