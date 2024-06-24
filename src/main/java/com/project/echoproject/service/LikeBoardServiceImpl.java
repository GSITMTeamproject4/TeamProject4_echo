package com.project.echoproject.service;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.LikeBoard;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.AuthBoardRepository;
import com.project.echoproject.repository.LikeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LikeBoardServiceImpl implements LikeBoardService {

    private final AuthBoardRepository authBoardRepository;
    private final LikeBoardRepository likeBoardRepository;

    @Override
    public void toggleLike(Long boardId, SiteUser siteUser) {
        AuthBoard authBoard = authBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + boardId));

        LikeBoard likeBoard = likeBoardRepository.findByAuthBoardAndSiteUser(authBoard, siteUser);

        if (likeBoard != null) {
            // 이미 좋아요를 누른 상태이므로 좋아요 취소
            likeBoardRepository.delete(likeBoard);
            authBoard.setLikeCount(authBoard.getLikeCount() - 1);
        } else {
            // 좋아요를 누르지 않은 상태이므로 좋아요 추가
            LikeBoard newLikeBoard = new LikeBoard();
            newLikeBoard.setAuthBoard(authBoard);
            newLikeBoard.setSiteUser(siteUser);
            likeBoardRepository.save(newLikeBoard);
            authBoard.setLikeCount(authBoard.getLikeCount() + 1);
        }

        authBoardRepository.save(authBoard);
    }

    @Override
    public int getLikeCount(Long boardId) {
        AuthBoard authBoard = authBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + boardId));
        return authBoard.getLikeCount();
    }
}
