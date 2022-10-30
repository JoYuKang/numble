package com.project.numble.core.profile;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private static final String REAL_1_PROFILE = "real1";
    private static final String REAL_2_PROFILE = "real2";
    private static final String DEFAULT_PROFILE = "default";

    private final Environment env;

    @GetMapping("/profile")
    public String getProfile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList(REAL_1_PROFILE, REAL_2_PROFILE);
        String defaultProfile = profiles.isEmpty() ? DEFAULT_PROFILE : profiles.get(0);

        return profiles.stream()
            .filter(realProfiles::contains)
            .findAny()
            .orElse(defaultProfile);
    }
}
