package com.project.numble.application.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindAddressByClientIpResponse {

    private final String addressName;
    private final String regionDepth1;
    private final String regionDepth2;
    private final String regionDepth3;
}
