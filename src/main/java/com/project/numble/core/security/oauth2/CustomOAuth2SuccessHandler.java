package com.project.numble.core.security.oauth2;

import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.core.security.CustomAuthenticationToken;
import com.project.numble.core.security.CustomUserDetails;
import com.project.numble.core.security.CustomUserDetailsService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String PROFILE = "profile";
    private static final String CONTENT_TYPE = "application/json";

    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        userRepository.findByEmail((String) oAuth2User.getAttributes().get(EMAIL))
            .ifPresentOrElse(account -> oAuth2LoginAccount(request, account),
                () -> oAuth2LoginAccount(request, userRepository.save(convertUser(oAuth2User))));
    }

    private void oAuth2LoginAccount(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(
            String.valueOf(user.getId()));

        SecurityContext context = SecurityContextHolder.getContext();

        context.setAuthentication(
            new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));

        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            context);
    }

    private User convertUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        return User.createSocialUser(
            (String) attributes.get("email"),
            (String) attributes.get("nickname"),
            (String) attributes.get("profile"));
    }
}
