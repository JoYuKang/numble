package com.project.numble.application.user.controller;

import com.project.numble.application.user.dto.response.FindAddressByClientIpResponse;
import com.project.numble.application.user.dto.response.FindAddressByQueryResponse;
import com.project.numble.application.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/address/search/ip")
    public ResponseEntity<FindAddressByClientIpResponse> searchAddressByIp() {
        return new ResponseEntity<>(userService.searchAddressByIp(), HttpStatus.OK);
    }

    @GetMapping("/address/search/query")
    public ResponseEntity<FindAddressByQueryResponse> searchAddressByQuery(@RequestParam String query) {
        return new ResponseEntity<>(userService.searchAddressByQuery(query), HttpStatus.OK);
    }
}
