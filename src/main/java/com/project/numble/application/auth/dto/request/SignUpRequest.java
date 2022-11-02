package com.project.numble.application.auth.dto.request;

import com.project.numble.application.user.domain.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequest {

    @NotBlank(message = "{email.notBlank}")
    @Email(message = "{email.email}")
    private String email;

    @NotBlank(message = "{password.notBlank}")
    private String password;

    @NotBlank(message = "{nickname.notBlank}")
    private String nickname;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.createNormalUser(email, passwordEncoder.encode(password), nickname);
    }
}
