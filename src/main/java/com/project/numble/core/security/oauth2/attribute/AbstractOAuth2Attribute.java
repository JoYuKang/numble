package com.project.numble.core.security.oauth2.attribute;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractOAuth2Attribute {

    protected Map<String, Object> attributes;
    protected String attributeKey;
    protected String email;
    protected String nickname;
    protected String profile;

    public Map<String, Object> convert() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("nickname", nickname);
        map.put("email", email);
        map.put("profile", profile);

        return map;
    }
}
