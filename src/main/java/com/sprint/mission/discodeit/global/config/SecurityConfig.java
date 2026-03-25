package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.auth.handler.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.auth.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.auth.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig
{
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           CustomAccessDeniedHandler customAccessDeniedHandler,
                                           LoginSuccessHandler loginSuccessHandler,
                                           LoginFailureHandler loginFailureHandler) throws Exception
    {
        http
            // 1. CSRF 설정
            .csrf(csrf -> csrf
                    // 쿠키 기반 CSRF 토큰 저장소
                    // JS에서 쿠키를 읽을 수 있도록 HttpOnly=false
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    // SPA 환경에 적합한 SpaCsrfTokenRequestHandler로 교체
                    .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
            )
            // 2. HTTP 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/", "/index.html", "/favicon.ico", "/assets/**",
                            "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**"
                    ).permitAll()
                    .requestMatchers("/api/auth/csrf-token").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                    .requestMatchers("/api/auth/login").permitAll()
                    .requestMatchers("/api/auth/logout").permitAll()
                    .anyRequest().authenticated()
            )
            // 3. Form 기반 로그인 설정
            .formLogin(login -> login
                    .loginProcessingUrl("/api/auth/login")
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler)
            )
            // 4. 로그아웃 설정
            .logout(logout -> logout
                    .logoutUrl("/api/auth/logout")
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
            )
            // 5. 예외 처리 설정
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                    .accessDeniedHandler(customAccessDeniedHandler)
            );

        return http.build();
    }
}
