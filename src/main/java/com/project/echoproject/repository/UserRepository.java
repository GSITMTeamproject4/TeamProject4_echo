package com.project.echoproject.repository;

import java.util.Optional;

import com.project.echoproject.entity.SiteUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long>{
    Optional<SiteUser>findByUsername(String username);
//optional_null값 에러 처리안함.(유연함.)
}
