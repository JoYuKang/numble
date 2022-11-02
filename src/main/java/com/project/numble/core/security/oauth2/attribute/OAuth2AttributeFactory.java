package com.project.numble.core.security.oauth2.attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class OAuth2AttributeFactory {

    private static final String KAKAO_ATTRIBUTE_KEY = "id";
    private static final String NAVER_ATTRIBUTE_KEY = "email";

    static final Map<String, Function<Map<String, Object>, AbstractOAuth2Attribute>> oauth2AttributeMap
            = new HashMap<>();

    static {
        oauth2AttributeMap.put("kakao",
                (Map<String, Object> attributes) -> KakaoOAuth2Attribute.createAttribute(KAKAO_ATTRIBUTE_KEY, attributes));
        oauth2AttributeMap.put("naver",
                (Map<String, Object> attributes) -> NaverOAuth2Attribute.createAttribute(NAVER_ATTRIBUTE_KEY, attributes));
    }

    public static AbstractOAuth2Attribute getOAuth2Attribute(String provider, Map<String, Object> attributes) {
        return oauth2AttributeMap.get(provider).apply(attributes);
    }
}
