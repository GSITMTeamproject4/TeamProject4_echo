
package com.project.echoproject.service;

import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SiteUserSecurityService extends UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
