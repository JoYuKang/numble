package com.project.numble.application.auth.service;

import com.project.numble.application.auth.dto.request.SignInRequest;
import com.project.numble.application.auth.service.exception.SignInFailureException;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.repository.exception.UserNotFoundException;
import com.project.numble.core.security.CustomAuthenticationToken;
import com.project.numble.core.security.CustomUserDetails;
import com.project.numble.core.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StandardAuthService implements AuthService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public void signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new SignInFailureException();
        }

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(
            String.valueOf(user.getId()));
        setAuthentication(userDetails);
    }

    private void setAuthentication(CustomUserDetails userDetails) {
        SecurityContextHolder
            .getContext()
            .setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }
}
