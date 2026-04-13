package com.sprint.mission.discodeit.auth.handler;

import com.sprint.mission.discodeit.global.secutiry.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtLogoutHandler implements LogoutHandler
{
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie refreshTokenCookie = jwtTokenProvider.genereateRefreshTokenExpirationCookie();
        response.addCookie(refreshTokenCookie);
    }
}
