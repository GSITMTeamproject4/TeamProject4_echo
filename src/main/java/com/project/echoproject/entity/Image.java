package com.project.echoproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    private String filePath; // 파일 경로를 저장할 필드

    private LocalDateTime uploadTime;

    @Transient
    private String base64; // Base64 문자열 저장할 필드

}
