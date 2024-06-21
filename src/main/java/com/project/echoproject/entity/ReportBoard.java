package com.project.echoproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ReportBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "listId", nullable = false)
    private AuthBoard authBoard;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private SiteUser siteUser;

    @Column(length = 50, nullable = false)
    private String reportReason;

    @Column(length = 300, nullable = false)
    private String reportContent;

    private LocalDateTime reportDate;

    @PrePersist
    protected void onCreate() {
        this.reportDate = LocalDateTime.now();
    }
}
