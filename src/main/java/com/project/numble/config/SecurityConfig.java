package com.project.numble.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(0)
    public SecurityFilterChain resource(HttpSecurity http) throws Exception {
        http
            .requestMatchers(matchers -> matchers
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()))
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
            .authorizeHttpRequests(authorize -> authorize
                .antMatchers(HttpMethod.GET, "/profile").permitAll()
                .antMatchers(HttpMethod.GET, "/application/health").permitAll()
                .anyRequest().authenticated());

        return http.build();
    }
}
