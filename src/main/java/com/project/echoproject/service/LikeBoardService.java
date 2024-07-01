package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;

public interface LikeBoardService {
    void toggleLike(Long boardId, SiteUser siteUser);
    int getLikeCount(Long boardId);
    boolean isLikedByUser(Long boardId, SiteUser siteUser);
}
