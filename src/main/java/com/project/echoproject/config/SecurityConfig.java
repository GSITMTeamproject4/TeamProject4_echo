package com.project.echoproject.config;

import com.project.echoproject.oauth.CustomAuthenticationSuccessHandler;
import com.project.echoproject.oauth.CustomOAuth2UserServiceImpl;
import com.project.echoproject.oauth.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    @Lazy
    private CustomUserDetailsService userDetailsService;

    @Autowired
    @Lazy
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    @Lazy
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private AuthProviderConfig authProviderConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, @Lazy CustomOAuth2UserServiceImpl customOAuth2UserService) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/challenge/add").authenticated()
                        .requestMatchers("/mall/buy/{id}").authenticated()
                        .requestMatchers("/payment/validation/**").authenticated()
                        .requestMatchers("/authBoard/create", "/authBoard/modify/**", "/authBoard/delete/**", "/authBoard/report/**").authenticated()
                        .requestMatchers("/admin/admin/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/index")
                        .failureHandler(customAuthenticationFailureHandler)
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/index")
                        .invalidateHttpSession(true)
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/access-denied")
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/user/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(authProviderConfig.passwordEncoder());
    }
}