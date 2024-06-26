package com.project.echoproject.dto;

import com.project.echoproject.entity.Challenge;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChallengeDTO {
    List<Challenge> challenge;
    Long challengeCnt;
}
