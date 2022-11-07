package com.project.numble.core.resolver;

import lombok.Getter;

@Getter
public class UserInfo {

    private Long userId;

    public UserInfo(Long userId) {
        this.userId = userId;
    }
}
