package com.project.numble.config;

import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.core.security.CustomUserDetailsService;
import com.project.numble.core.security.oauth2.CustomAuthenticationFailureHandler;
import com.project.numble.core.security.oauth2.CustomLoginFailureEntryPoint;
import com.project.numble.core.security.oauth2.CustomOAuth2SuccessHandler;
import com.project.numble.core.security.oauth2.CustomOAuth2UserService;
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
import org.springframework.security.config.http.SessionCreationPolicy;
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
            .exceptionHandling()
            .authenticationEntryPoint(new CustomLoginFailureEntryPoint())
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and()
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
                .antMatchers("/", "/**").permitAll()
                .anyRequest().authenticated())
            .oauth2Login()
            .userInfoEndpoint(oauth2 -> oauth2.userService(oAuth2UserService()))
            .successHandler(oAuth2SuccessHandler())
            .failureHandler(new CustomAuthenticationFailureHandler());

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

    @Bean
    public CustomOAuth2UserService oAuth2UserService() {
        return new CustomOAuth2UserService();
    }

    @Bean
    public CustomOAuth2SuccessHandler oAuth2SuccessHandler() {
        return new CustomOAuth2SuccessHandler(userRepository, customUserDetailsService());
    }
}
