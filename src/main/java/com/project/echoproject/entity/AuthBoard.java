package com.project.echoproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class AuthBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listId;

    @Column(length = 200, nullable = false)
    private String boardTitle;

    @Column(columnDefinition = "TEXT")
    private String boardContent;

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

    @OneToMany(mappedBy = "authBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeBoard> likeBoards;

    @OneToMany(mappedBy = "authBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportBoard> reportBoards;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "imageId", referencedColumnName = "id")
    private Image image;

    @PrePersist
    protected void onCreate() {
        this.postCreateDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.postModifyDate = LocalDateTime.now();
    }
}
