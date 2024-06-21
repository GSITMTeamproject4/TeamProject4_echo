package com.project.echoproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class LikeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "listId", nullable = false)
    private AuthBoard authBoard;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private SiteUser siteUser;

    private LocalDateTime likeDate;

    @PrePersist
    protected void onCreate() {
        this.likeDate = LocalDateTime.now();
    }

}
