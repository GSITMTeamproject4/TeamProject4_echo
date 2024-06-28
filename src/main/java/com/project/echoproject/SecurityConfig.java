package com.project.echoproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(formLogin -> formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/index"))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/index")
                        .invalidateHttpSession(true))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/access-denied")) // 접근 거부 시 이동할 페이지 설정
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/challenge/add").authenticated()
                        .requestMatchers("/mypage").authenticated()
                        // /challenge/add 페이지 접근에 대한 인증 필요
                        .requestMatchers("/mall/buy/{id}").authenticated()
                        .requestMatchers("/payment/validation/**").authenticated()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll()
                );
        return http.build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean  // 스프링 시큐리티의 인증을 처리, UserSecurityService와 PasswordEncoder를 내부적으로 사용하여 인증과 권한 부여를 처리한다
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
