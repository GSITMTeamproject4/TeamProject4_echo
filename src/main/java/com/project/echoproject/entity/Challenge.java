package com.project.echoproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeId;

    @ManyToOne
    private SiteUser userId;

    private String challengeInfo;

    private Date challengeDate;

    @Column(length = 200, nullable = false)
    private String checkImg;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private SiteUser siteUser;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "imageId", referencedColumnName = "id")
    private Image image;
}
