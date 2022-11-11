package com.project.numble.application.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.numble.application.user.domain.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetAddressResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String addressName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String regionDepth1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String regionDepth2;

    public static GetAddressResponse fromAddress(Address address) {
        return new GetAddressResponse(address.getAddressName(), address.getRegionDepth1(),
            address.getRegionDepth2());
    }
}
