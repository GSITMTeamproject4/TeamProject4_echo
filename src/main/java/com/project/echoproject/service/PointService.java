package com.project.echoproject.service;

import com.project.echoproject.entity.Point;
import com.project.echoproject.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;

    public void addPointHistory(Point point) {
        pointRepository.save(point);
    }

    //사용자의 전체 포인트 내역을 조회하여 최신 순으로 정렬된 리스트로 반환
    public List<Point> getPointHistoryByUserId(String userId) {
        return pointRepository.findBySiteUser_UserIdOrderByInsertDateDesc(userId);
    }
}








