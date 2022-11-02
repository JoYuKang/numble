package com.project.numble.core.security.oauth2.attribute;

import java.util.Map;
import lombok.Builder;

public class NaverOAuth2Attribute extends AbstractOAuth2Attribute {

    private static final String RESPONSE_TARGET = "response";
    private static final String NAVER_EMAIL = "email";
    private static final String NAVER_NICKNAME = "nickname";
    private static final String NAVER_PROFILE = "profile_image";

    @Builder
    private NaverOAuth2Attribute(Map<String, Object> attributes, String attributeKey,
                                 String email, String nickname, String profile) {
        this.attributes = attributes;
        this.attributeKey = attributeKey;
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
    }

    public static NaverOAuth2Attribute createAttribute(String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get(RESPONSE_TARGET);

        return NaverOAuth2Attribute.builder()
                .attributes(response)
                .attributeKey(attributeKey)
                .email((String) response.get(NAVER_EMAIL))
                .nickname((String) response.get(NAVER_NICKNAME))
                .profile((String) response.get(NAVER_PROFILE))
                .build();
    }
}
