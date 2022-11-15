package com.project.numble.application.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.domain.enums.AnimalType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetMyInfoResponse {

    private String nickname;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profile;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String regionDepth2;

    private List<String> animals;

    public static GetMyInfoResponse fromUser(User user) {
        return GetMyInfoResponse.builder()
            .nickname(user.getNickname())
            .profile(user.getProfile())
            .regionDepth2(Objects.isNull(user.getAddress()) ? null : user.getAddress().getRegionDepth2())
            .animals(user.getAnimals().stream().map(animal -> AnimalType.getName(animal.getAnimalType())).collect(
                Collectors.toList()))
            .build();
    }
}
