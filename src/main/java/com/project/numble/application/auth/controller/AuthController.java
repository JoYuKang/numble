package com.project.numble.application.auth.controller;

import com.project.numble.application.auth.dto.request.SignInRequest;
import com.project.numble.application.auth.dto.request.SignUpRequest;
import com.project.numble.application.auth.exception.UserAlreadySignOutException;
import com.project.numble.application.auth.service.AuthService;
import com.project.numble.application.user.repository.exception.UserNotFoundException;
import com.project.numble.application.user.service.UserService;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        userService.signUp(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@Validated @RequestBody SignInRequest request,
        HttpSession session) {
        authService.signIn(request);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/sign-out")
    public ResponseEntity<Void> signOut(HttpSession session) {
        Object attribute = session.getAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

        if (Objects.isNull(attribute)) {
            throw new UserNotFoundException();
        }

        try {
            session.invalidate();
        } catch (IllegalStateException e) {
            throw new UserAlreadySignOutException(e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
