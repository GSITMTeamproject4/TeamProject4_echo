package com.project.echoproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UseAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "site_user_id")
    private SiteUser siteUser;

    @Column(columnDefinition = "LONG DEFAULT 0")
    private long useElectricity;

    @Column(columnDefinition = "LONG DEFAULT 0")
    private long useEleYear;

    @Column(columnDefinition = "LONG DEFAULT 0")
    private long useEleMoth;

    @Column(columnDefinition = "LONG DEFAULT 0")
    private long useGas;

    @Column(columnDefinition = "LONG DEFAULT 0")
    private long useGasYear;

    @Column(columnDefinition = "LONG DEFAULT 0")
    private long useGasMoth;
}
