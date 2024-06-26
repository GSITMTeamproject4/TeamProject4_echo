package com.project.echoproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PointDTO {
    private Long pointId;
    private String userId;
    private Long point;
    private String pointInfo;
    private LocalDateTime insertDate;
}