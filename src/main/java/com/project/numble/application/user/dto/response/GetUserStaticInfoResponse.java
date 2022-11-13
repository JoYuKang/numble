package com.project.numble.application.user.dto.response;

import com.project.numble.application.user.domain.Address;
import com.project.numble.application.user.domain.User;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class GetUserStaticInfoResponse {

    private String email;
    private GetAddressResponse address;
    private String nickname;
    private boolean hasPet;
    private boolean isFirst;

    public static GetUserStaticInfoResponse fromUser(User user, long count) {
        return GetUserStaticInfoResponse.builder()
                .email(user.getEmail())
                .address(GetAddressResponse.fromAddress(Objects.isNull(user.getAddress()) ? new Address() : user.getAddress()))
                .nickname(user.getNickname())
                .hasPet(!CollectionUtils.isEmpty(user.getAnimals()))
                .isFirst(count < 2)
                .build();
    }
}
