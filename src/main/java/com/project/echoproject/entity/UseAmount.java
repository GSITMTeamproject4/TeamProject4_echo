package com.project.echoproject.entity;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class UseAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long useid;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private SiteUser siteUser;

    @Column(nullable = false, columnDefinition = "LONG default 0")
    private Long useElectricity = 0L;

    @Column(nullable = false, columnDefinition = "LONG default 0")
    private Long useGas = 0L;

    @Column(nullable = false)
    private LocalDate useDate;

}

