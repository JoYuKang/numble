package com.project.numble.application.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FindAddressByQueryResponse {

    private final String regionDepth1;
    private final String regionDepth2;

    public FindAddressByQueryResponse() {
        this.regionDepth1 = null;
        this.regionDepth2 = null;
    }
}
