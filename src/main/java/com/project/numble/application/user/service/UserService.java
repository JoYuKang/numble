package com.project.numble.application.user.service;

import com.project.numble.application.auth.dto.request.SignUpRequest;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.service.exception.UserEmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequest request) {
        validateSignUp(request);

        userRepository.save(request.toUser(passwordEncoder));
    }

    private void validateSignUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserEmailAlreadyExistsException();
        }
    }
}
