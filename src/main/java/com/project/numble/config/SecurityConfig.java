package com.project.numble.config;

import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.core.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    @Order(0)
    public SecurityFilterChain resource(HttpSecurity http) throws Exception {
        http
            .requestMatchers(matchers -> matchers
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/docs/**"))
            .authorizeRequests(authorize -> authorize.anyRequest().permitAll())
            .requestCache(RequestCacheConfigurer::disable)
            .securityContext(AbstractHttpConfigurer::disable)
            .sessionManagement(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .headers()
            .frameOptions()
            .sameOrigin()
            .and()
            .authorizeHttpRequests(authorize -> authorize
                .antMatchers(HttpMethod.GET, "/profile").permitAll()
                .antMatchers(HttpMethod.GET, "/application/health").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/sign-up").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/sign-in").permitAll()
                .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }
}
