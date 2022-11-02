package com.project.numble.core.security.oauth2;

import com.project.numble.application.user.domain.enums.Role;
import com.project.numble.core.security.oauth2.attribute.AbstractOAuth2Attribute;
import com.project.numble.core.security.oauth2.attribute.OAuth2AttributeFactory;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String NAME_ATTRIBUTE_KEY = "email";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        AbstractOAuth2Attribute oAuth2Attribute =
                OAuth2AttributeFactory.getOAuth2Attribute(registrationId, oAuth2User.getAttributes());

        Map<String, Object> accountAttribute = oAuth2Attribute.convert();

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_USER.name())),
                accountAttribute,
                NAME_ATTRIBUTE_KEY
        );
    }
}
