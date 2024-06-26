package com.project.echoproject.service;

import com.project.echoproject.entity.Point;
import com.project.echoproject.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;

    public void addPointHistory(Point point) {
        pointRepository.save(point);
    }

    // 해당 사용자의 전체 포인트 내역을 조회하여 최신 순으로 정렬된 리스트로 반환
    public List<Point> getPointHistoryByUserId(String userId) {
        return pointRepository.findBySiteUser_UserIdOrderByInsertDateDesc(userId);
    }

    // 페이징 처리된 포인트 내역 조회
    public Page<Point> getPointHistoryByUserIdPaged(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("insertDate").descending());
        return pointRepository.findBySiteUser_UserIdOrderByInsertDateDesc(userId, pageable);
    }

    // 사용자의 전체 포인트 내역 개수 조회
    public long countPointHistoryByUserId(String userId) {
        return pointRepository.countBySiteUser_UserId(userId);
    }
}