package kr.minimalest.api.web.controller;

import jakarta.validation.Valid;
import kr.minimalest.api.application.user.SignUp;
import kr.minimalest.api.application.user.SignUpArgument;
import kr.minimalest.api.application.user.SignUpResult;
import kr.minimalest.api.web.controller.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final SignUp signUp;

    @PostMapping
    public ResponseEntity<?> signUp(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        SignUpArgument signUpArgument = SignUpArgument.of(signUpRequest.email(), signUpRequest.password());
        SignUpResult result = signUp.exec(signUpArgument);

        return ResponseEntity.ok(
                Map.of("userUUID", result.userId().id())
        );
    }
}
