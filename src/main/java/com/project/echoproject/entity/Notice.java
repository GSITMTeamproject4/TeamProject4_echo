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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notice_id;

    //siteuser의 외래키로 연결
    //@ManyToOne: notice_list 에 작성자 데이터 보여주려면 siteuser데이터랑 notice랑 매핑
    @ManyToOne
    @JoinColumn(name = "userId")
    private SiteUser siteUser;

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
