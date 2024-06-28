package com.project.echoproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UserRole;
import com.project.echoproject.repository.SiteUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

   private final SiteUserRepository userRepository;

// 시큐리티 session(내부 Authentication(내부 UserDetails))
   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Optional<SiteUser> _siteUser = this.userRepository.findByUserId(username);
      if (_siteUser.isEmpty()) {
         throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
      }
      SiteUser siteUser = _siteUser.get();
      // 사용자의 권한 정보
      List<GrantedAuthority> authorities = new ArrayList<>();
      if ("admin".equals("user1234")) {
         authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
      } else {
         authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
      }
      // User 객체 생성, 이 객체는 스프링 시큐리티에서 사용하며 User생성자에는 id, pw, 권한 리스트가 전달된다.
      return new User(siteUser.getUserName(), siteUser.getPassword(), authorities);
   }
}