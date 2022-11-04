package com.project.numble.application.auth.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignInRequest {

    @NotBlank(message = "{email.notBlank}")
    @Email(message = "{email.email}")
    private String email;

    @NotBlank(message = "{password.notBlank}")
    private String password;
}
