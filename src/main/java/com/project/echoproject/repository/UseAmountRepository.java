package com.project.echoproject.repository;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UseAmount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UseAmountRepository extends JpaRepository<UseAmount, Long> {
    // 사용자의 특정 월의 데이터를 조회
    Optional<UseAmount> findFirstBySiteUserAndUseDateBetween(SiteUser siteUser, LocalDate startDate, LocalDate endDate);

    // 사용자의 특정 연도의 모든 데이터를 조회
    List<UseAmount> findBySiteUserAndUseDateBetween(SiteUser siteUser, LocalDate startDate, LocalDate endDate);
}