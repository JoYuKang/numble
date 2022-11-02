package com.project.numble.core.security;

import com.project.numble.application.user.repository.UserRepository;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.SessionRepository;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends GenericFilterBean {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();

        Object attribute = session.getAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

        if (attribute != null) {
            SecurityContext context = (SecurityContext) attribute;
            String name = context.getAuthentication().getName();
            System.out.println("name = " + name);
        }

        /*
        userRepository
            .findByEmail(email)
            .filter(user -> passwordEncoder.matches(user.getPassword(), password))
            .map(user -> userDetailsService.loadUserByUsername(String.valueOf(user.getId())))
            .ifPresent(this::setAuthentication);

         */

        chain.doFilter(req, res);
    }

    private void setAuthentication(CustomUserDetails userDetails) {
        SecurityContextHolder
                .getContext()
                .setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }

    private Optional<String> extractCookie(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Cookie[] cookies = httpServletRequest.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("SESSION")) {
                return Optional.of(cookie.getValue());
            }
        }

        return Optional.empty();
    }

    private Optional<String> extractToken(ServletRequest request) {
        return Optional.ofNullable(((HttpServletRequest)request).getHeader("Authorization"));
    }
}
