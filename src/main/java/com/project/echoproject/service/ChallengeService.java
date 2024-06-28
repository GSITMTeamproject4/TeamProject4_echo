package com.project.echoproject.service;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.Challenge;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.exception.NoChallengeFoundException;
import com.project.echoproject.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ImageService imageService;

//    public List<Challenge> getUserChallenge(String userId) {
//        List<Challenge> userChall = challengeRepository.findAllByUserId(userId);
//
//        if(userChall != null || !userChall.isEmpty()){
//            return userChall;
//        }else {
//            throw new UsernameNotFoundException("챌린지를 찾을수 없습니다.");
//        }
//
//    }

//    public Long getUserChallengeCnt(String userId) {
//        return challengeRepository.CountByUserId(userId);
//    }

//
//    public List<Challenge> findAll(String id) {
//        return challengeRepository.findAll();
//    }

    public List<Map<String, Object>> getChallengeList(SiteUser siteUser) {
        //Map<String, Object> chall = new HashMap<String, Object>();
        List<Map<String, Object>> challList = new ArrayList<Map<String, Object>>();

        List<Challenge> userChall = challengeRepository.findBySiteUser_UserId(siteUser.getUserId());
        for(Challenge challenge : userChall) {
            Map<String, Object> chall = new HashMap<String, Object>();
            chall.put("start",challenge.getChallengeDate());
            chall.put("end",challenge.getChallengeDate());
            chall.put("title",challenge.getChallengeInfo());
            challList.add(chall);
        }
        return challList;
    }

    public void addChallImg(SiteUser siteUser, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            // ImageService를 사용하여 이미지 저장
            Image image = imageService.saveImage(file);
            Challenge challenge = new Challenge();
//            challenge.setImage(image);
//            challenge.setCheckImg(image.getFilePath()); // checkImg 필드 설정
            challenge.setSiteUser(siteUser); // siteUser 설정
        }else {
            throw new IOException("파일 없음");
        }
    }


    public Challenge createBoard(String title,  MultipartFile file, SiteUser siteUser,String content) throws IOException {

        Challenge challenge = new Challenge();
        Date date = new Date();
        challenge.setChallengeDate(date);
        challenge.setChallengeInfo(title);
        challenge.setChallengeContent(content);
        if (!file.isEmpty()) {
            // ImageService를 사용하여 이미지 저장
            Image image = imageService.saveImage(file);
            challenge.setImage(image);
            challenge.setCheckImg(image.getFilePath()); // checkImg 필드 설정
            challenge.setSiteUser(siteUser); // siteUser 설정
        } else {
            throw new IOException("파일 없음");
        }

        return challengeRepository.save(challenge);
    }

    public List<Challenge> getChallengAll() {
        List<Challenge> challenges = challengeRepository.findAllByOrderByChallengeDate();
        if(challenges.isEmpty()) {
            throw new NoChallengeFoundException("확인할 챌린지가 없습니다.");
        }
        return challenges;
    }

    public void deleteChallenge(Long id) {
        challengeRepository.deleteById(id);
    }

    public Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id).get();
    }

}
