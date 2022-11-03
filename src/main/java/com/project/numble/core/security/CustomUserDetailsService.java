package com.project.numble.core.security;

import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return convert(userRepository.findById(Long.parseLong(userId)).orElseThrow());
    }

    private CustomUserDetails convert(User user) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(
            user.getRole().name());
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(simpleGrantedAuthority);
        return new CustomUserDetails(String.valueOf(user.getId()), authorities);
    }
}
