package com.project.echoproject.service;

import com.project.echoproject.entity.Point;
import com.project.echoproject.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;

    public void addPointHistory(Point point) {
       pointRepository.save(point);
    }
}