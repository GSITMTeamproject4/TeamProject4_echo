package com.project.echoproject.repository;

import com.project.echoproject.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
//    List<Challenge> findByUserIdUserId(String userId);
    List<Challenge> findBySiteUser_UserId(String userid);
    List<Challenge> findAllByOrderByChallengeDate();
    //Long CountByUserId(String userId);

    //Challenge findByUserId(String id);

    //@Override
    //Optional<Challenge> findById(Long aLong);
}
