package com.project.echoproject.oauth;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UserRole;
import com.project.echoproject.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomOAuth2UserServiceImpl(SiteUserRepository siteUserRepository, PasswordEncoder passwordEncoder) {
        this.siteUserRepository = siteUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String generateTemporaryPassword() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId;
        String email;
        String name;

        if ("google".equals(provider)) {
            providerId = oauth2User.getAttribute("sub");
            email = oauth2User.getAttribute("email");
            name = oauth2User.getAttribute("name");
        } else if ("naver".equals(provider)) {
            Map<String, Object> response = (Map<String, Object>) oauth2User.getAttributes().get("response");
            providerId = (String) response.get("id");
            email = (String) response.get("email");
            name = (String) response.get("name");
        } else if ("kakao".equals(provider)) {
            Long id = oauth2User.getAttribute("id");
            providerId = (id != null) ? id.toString() : null;
            Map<String, Object> kakaoAccount = (Map<String, Object>) oauth2User.getAttributes().get("kakao_account");
            email = (String) kakaoAccount.get("email");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            name = (String) profile.get("nickname");
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
        }

        // 이름이 없으면 임의로 생성
        if (name == null || name.isEmpty()) {
            name = provider + "User_" + providerId.substring(0, Math.min(providerId.length(), 5));
        }

        String userId = provider + "_" + providerId;

        SiteUser siteUser = siteUserRepository.findByUserIdAndProvider(userId, provider)
                .orElse(new SiteUser());

        if (siteUser.getUserId() == null) {
            String tempPassword = generateTemporaryPassword();

            siteUser.setUserId(userId);
            siteUser.setUserName(name);
            siteUser.setEmail(email);
            siteUser.setProvider(provider);
            siteUser.setProviderId(providerId);
            siteUser.setRole(UserRole.USER);
            siteUser.setPassword(passwordEncoder.encode(tempPassword));
            siteUserRepository.save(siteUser);

            // TODO: 임시 비밀번호를 사용자에게 안전하게 전달하는 로직 추가 (예: 이메일 발송)
        }

        if (siteUser.getUserId() != null) {
            siteUser.setUserName(name);
            siteUser.setEmail(email);
            siteUserRepository.save(siteUser);
        }

        return new CustomOAuth2User(oauth2User, userId, email, name,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + siteUser.getRole().name())));
    }
}