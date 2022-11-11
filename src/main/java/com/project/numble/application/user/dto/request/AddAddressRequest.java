package com.project.numble.application.user.dto.request;

import com.project.numble.application.user.domain.Address;
import com.project.numble.application.user.domain.User;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddAddressRequest {

    @NotEmpty(message = "{addressName.notEmpty}")
    private String addressName;

    @NotEmpty(message = "{regionDepth1.notEmpty}")
    private String regionDepth1;

    @NotEmpty(message = "{regionDepth2.notEmpty}")
    private String regionDepth2;

    public static Address toAddress(AddAddressRequest request, User user) {
        return new Address(request.addressName, request.regionDepth1, request.regionDepth2, user);
    }
}
