package com.project.numble.core.security;

import com.project.numble.core.security.exception.UnsupportedSecurityOperationException;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private CustomUserDetails principal;

    public CustomAuthenticationToken(CustomUserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedSecurityOperationException();
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
