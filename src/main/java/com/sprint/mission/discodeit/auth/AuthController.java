package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.auth.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken)
    {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청: {}", tokenValue);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal DiscodeitUserDetails discodeitUserDetails)
    {
        log.debug("AuthController 세션 기반 사용자 정보 조회 요청: {}", discodeitUserDetails.getUsername());

        UserDto userDto = authService.getCurrentUseInfo(discodeitUserDetails);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.from(userDto));
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
