package com.project.echoproject.repository;

import com.project.echoproject.entity.LikeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, String> {
}
