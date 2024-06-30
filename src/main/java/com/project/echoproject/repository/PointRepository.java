package com.project.echoproject.repository;

import com.project.echoproject.entity.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findBySiteUser_UserIdOrderByInsertDateDesc(String userId);
    Page<Point> findBySiteUser_UserIdOrderByInsertDateDesc(String userId, Pageable pageable);
    long countBySiteUser_UserId(String userId);
}
