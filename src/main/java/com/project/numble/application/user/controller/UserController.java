package com.project.numble.application.user.controller;

import com.project.numble.application.user.dto.request.AddAddressRequest;
import com.project.numble.application.user.dto.request.AddAnimalsRequest;
import com.project.numble.application.user.dto.response.FindAddressByClientIpResponse;
import com.project.numble.application.user.dto.response.FindAddressByQueryResponse;
import com.project.numble.application.user.dto.response.GetAddressResponse;
import com.project.numble.application.user.dto.response.GetMyInfoResponse;
import com.project.numble.application.user.dto.response.GetUserStaticInfoResponse;
import com.project.numble.application.user.service.UserService;
import com.project.numble.core.resolver.SignInUser;
import com.project.numble.core.resolver.UserInfo;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/address")
    public ResponseEntity<Void> addAddress(@SignInUser UserInfo userInfo, @Valid @RequestBody AddAddressRequest request) {
        userService.addAddress(userInfo, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/address")
    public ResponseEntity<GetAddressResponse> getAddress(@SignInUser UserInfo userInfo) {
        return new ResponseEntity<>(userService.getAddress(userInfo), HttpStatus.OK);
    }

    @DeleteMapping("/address")
    public ResponseEntity<Void> delAddress(@SignInUser UserInfo userInfo) {
        userService.delAddress(userInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/animals")
    public ResponseEntity<Void> addAnimals(@SignInUser UserInfo userInfo, @Valid @RequestBody
        AddAnimalsRequest request) {
        userService.addAnimals(userInfo, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/static-info")
    public ResponseEntity<GetUserStaticInfoResponse> getUserStaticInfo(@SignInUser UserInfo userInfo) {
        return new ResponseEntity<>(userService.getUserStaticInfo(userInfo), HttpStatus.OK);
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<Void> withdrawalUser(@SignInUser UserInfo userInfo) {
        userService.withdrawalUser(userInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/my-info")
    public ResponseEntity<GetMyInfoResponse> getMyInfo(@SignInUser UserInfo userInfo) {
        return new ResponseEntity<>(userService.getMyInfo(userInfo),HttpStatus.OK);
    }
}
