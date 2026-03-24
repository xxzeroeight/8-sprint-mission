package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.auth.service.LoginFailureHandler;
import com.sprint.mission.discodeit.auth.service.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig
{
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                    // 쿠키 기반 CSRF 토큰 저장소
                    // JS에서 쿠키를 읽을 수 있도록 HttpOnly=false
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    // SPA 환경에 적합한 SpaCsrfTokenRequestHandler로 교체
                    .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
            )
            .formLogin(login -> login
                    .loginProcessingUrl("/api/auth/login")
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler)
            );

        return http.build();
    }
}
