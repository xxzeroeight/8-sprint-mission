package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.auth.dto.request.LoginRequest;
import com.sprint.mission.discodeit.auth.dto.response.LoginResponse;
import com.sprint.mission.discodeit.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest)
    {
        UserDto user = authService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(LoginResponse.from(user));
    }
}
