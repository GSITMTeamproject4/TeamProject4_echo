package com.project.echoproject.oauth;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SiteUserRepository siteUserRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        SiteUser siteUser = siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        return new org.springframework.security.core.userdetails.User(siteUser.getUserId(), siteUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + siteUser.getRole().name())));
    }
}