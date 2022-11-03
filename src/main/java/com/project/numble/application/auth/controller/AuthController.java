package com.project.numble.application.auth.controller;

import com.project.numble.application.auth.dto.request.SignUpRequest;
import com.project.numble.application.user.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        userService.signUp(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
