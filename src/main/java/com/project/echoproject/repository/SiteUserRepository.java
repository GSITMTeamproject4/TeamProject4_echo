package com.project.echoproject.repository;

import com.project.echoproject.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteUserRepository extends JpaRepository<SiteUser, String> {
}
