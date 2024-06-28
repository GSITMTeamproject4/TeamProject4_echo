package com.project.echoproject.repository;

import com.project.echoproject.entity.Challenge;
import com.project.echoproject.entity.Point;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    //List<Challenge> findByUserId(String userId);
    List<Challenge> findBySiteUser_UserId(String userid);
    List<Challenge> findAllByOrderByChallengeDate();


}
