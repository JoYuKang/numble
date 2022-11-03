package com.project.numble.core.security.oauth2.attribute;

import java.util.Map;
import lombok.Builder;

public class KakaoOAuth2Attribute extends AbstractOAuth2Attribute {

    private static final String RESPONSE_TARGET_ACCOUNT = "kakao_account";
    private static final String RESPONSE_TARGET_PROFILE = "profile";
    private static final String KAKAO_ID = "id";
    private static final String KAKAO_NICKNAME = "nickname";
    private static final String KAKAO_PROFILE = "profile_image_url";

    @Builder
    private KakaoOAuth2Attribute(Map<String, Object> attributes, String attributeKey,
                                 String email, String nickname, String profile) {
        this.attributes = attributes;
        this.attributeKey = attributeKey;
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
    }

    public static KakaoOAuth2Attribute createAttribute(String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get(RESPONSE_TARGET_ACCOUNT);
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get(RESPONSE_TARGET_PROFILE);

        return KakaoOAuth2Attribute.builder()
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .email(String.valueOf(attributes.get(KAKAO_ID)))
                .nickname((String) kakaoProfile.get(KAKAO_NICKNAME))
                .profile((String) kakaoProfile.get(KAKAO_PROFILE))
                .build();
    }
}
