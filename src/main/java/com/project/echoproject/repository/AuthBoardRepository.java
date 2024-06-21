package com.project.echoproject.repository;

import com.project.echoproject.entity.AuthBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthBoardRepository extends JpaRepository<AuthBoard, Long> {
}
