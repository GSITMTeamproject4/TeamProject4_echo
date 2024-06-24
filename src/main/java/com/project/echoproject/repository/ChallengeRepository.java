package com.project.echoproject.repository;

import com.project.echoproject.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    //List<Challenge> findAllByUserId(String userId);
    //Long CountByUserId(String userId);
}
