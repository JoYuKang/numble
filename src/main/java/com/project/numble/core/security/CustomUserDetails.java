package com.project.numble.core.security;

import com.project.numble.core.security.exception.UnsupportedSecurityOperationException;
import java.util.Collection;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final String userId;
    private final Set<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        throw new UnsupportedSecurityOperationException();
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        throw new UnsupportedSecurityOperationException();
    }

    @Override
    public boolean isAccountNonLocked() {
        throw new UnsupportedSecurityOperationException();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        throw new UnsupportedSecurityOperationException();
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedSecurityOperationException();
    }
}
