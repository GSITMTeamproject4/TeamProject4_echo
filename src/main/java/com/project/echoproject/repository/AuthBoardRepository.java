package com.project.echoproject.repository;

import com.project.echoproject.entity.AuthBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthBoardRepository extends JpaRepository<AuthBoard, Long> {
    //페이징 처리
    Page<AuthBoard> findAll(Pageable pageable);

    Page<AuthBoard> findBySiteUser_UserId(String userId, Pageable pageable);


}
