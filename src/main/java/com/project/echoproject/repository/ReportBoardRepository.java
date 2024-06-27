package com.project.echoproject.repository;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.ReportBoard;
import com.project.echoproject.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportBoardRepository extends JpaRepository<ReportBoard, Long> {
    Optional<ReportBoard> findByAuthBoardAndSiteUser(AuthBoard authBoard, SiteUser siteUser);

}
