package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.auth.dto.info.JwtInformation;
import com.sprint.mission.discodeit.auth.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.auth.dto.response.JwtDto;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.global.secutiry.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final UserService userService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken)
    {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청: {}", tokenValue);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> reissue(@CookieValue("REFRESH_TOKEN") String refreshToken, HttpServletResponse response)
    {
        JwtInformation jwtInformation = authService.refreshToken(refreshToken);
        Cookie refreshCookie = jwtTokenProvider.genereateRefreshTokenCookie(jwtInformation.refreshToken());
        response.addCookie(refreshCookie);

        JwtDto jwtDto = new JwtDto(
                jwtInformation.userDto(),
                jwtInformation.accessToken()
        );

        return ResponseEntity.status(HttpStatus.OK).body(jwtDto);
    }

    @PutMapping("/role")
    public ResponseEntity<UserResponse> updateRole(@RequestBody @Valid RoleUpdateRequest roleUpdateRequest)
    {
        log.debug("AuthContoller 사용자 권한 변경 요청: {}", roleUpdateRequest.userId());

        UserDto userDto = userService.updateRole(roleUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.from(userDto));
    }
}
