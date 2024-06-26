package com.project.echoproject.repository;

import com.project.echoproject.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface SiteUserRepository extends JpaRepository<SiteUser, String> {
    Optional<SiteUser> findByUserId(String userId);

}
