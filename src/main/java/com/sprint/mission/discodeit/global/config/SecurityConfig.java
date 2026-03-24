package com.sprint.mission.discodeit.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                    // 쿠키 기반 CSRF 토큰 저장소
                    // JS에서 쿠키를 읽을 수 있도록 HttpOnly=false
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    // SPA 환경에 적합한 SpaCsrfTokenRequestHandler로 교체
                    .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
            );

        return http.build();
    }
}
