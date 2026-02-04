package com.sprint.mission.discodeit.global.secutiry.auth;

import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.global.secutiry.auth.dto.request.LoginRequest;
import com.sprint.mission.discodeit.global.secutiry.auth.dto.response.LoginResponse;
import com.sprint.mission.discodeit.global.secutiry.auth.service.AuthService;
import jakarta.validation.Valid;
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
public class AuthController implements AuthSwaggerApi
{
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest)
    {
        UserDto user = authService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(LoginResponse.from(user));
    }
}
