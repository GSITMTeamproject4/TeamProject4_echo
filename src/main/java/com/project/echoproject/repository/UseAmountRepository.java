package com.project.echoproject.repository;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UseAmount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UseAmountRepository extends JpaRepository<UseAmount, SiteUser> {
}
