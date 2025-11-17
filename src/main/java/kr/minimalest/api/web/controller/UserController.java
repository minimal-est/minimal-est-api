package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.minimalest.api.application.user.SignUp;
import kr.minimalest.api.application.user.SignUpArgument;
import kr.minimalest.api.application.user.SignUpResult;
import kr.minimalest.api.web.controller.dto.request.SignUpRequest;
import kr.minimalest.api.web.controller.dto.response.UserIdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {

    private final SignUp signUp;

    @PostMapping
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<UserIdResponse> signUp(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        SignUpArgument signUpArgument = SignUpArgument.of(signUpRequest.email(), signUpRequest.password());
        SignUpResult result = signUp.exec(signUpArgument);

        return ResponseEntity.ok(UserIdResponse.of(result.userId().id()));
    }
}
