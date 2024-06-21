package com.project.echoproject.repository;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.LikeBoard;
import com.project.echoproject.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, Long> {
    LikeBoard findByAuthBoardAndSiteUser(AuthBoard authBoard, SiteUser siteUser);
}
