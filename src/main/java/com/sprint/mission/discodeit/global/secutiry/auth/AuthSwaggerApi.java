package com.sprint.mission.discodeit.global.secutiry.auth;

import com.sprint.mission.discodeit.global.secutiry.auth.dto.request.LoginRequest;
import com.sprint.mission.discodeit.global.secutiry.auth.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "Auth API")
public interface AuthSwaggerApi
{
    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "user를 찾을 수 없음.",
                    content = @Content(examples = @ExampleObject(value = "user를 찾을 수 없음."))
            ),
            @ApiResponse(
                    responseCode = "400", description = "아이디 또는 비밀번호가 일치하지 않음.",
                    content = @Content(examples = @ExampleObject(value = "아이디 또는 비밀번호가 일치하지 않음."))
            )
    })
    ResponseEntity<LoginResponse> login(
            @Parameter(description = "로그인 정보") LoginRequest loginRequest
    );
}
