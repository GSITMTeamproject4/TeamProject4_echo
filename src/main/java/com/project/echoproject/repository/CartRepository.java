package com.project.echoproject.repository;

import com.project.echoproject.entity.Cart;
import com.project.echoproject.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(SiteUser siteUser);
}
