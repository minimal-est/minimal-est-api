package kr.minimalest.api.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.minimalest.api.application.auth.AuthenticateUser;
import kr.minimalest.api.application.auth.AuthenticateUserArgument;
import kr.minimalest.api.application.auth.JwtAuthResult;
import kr.minimalest.api.web.SafeCookie;
import kr.minimalest.api.web.dto.IssueTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUser authenticateUser;
    private final String REFRESH_TOKEN = "refreshToken";

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> issueToken(
            HttpServletResponse response,
            @RequestBody @Valid IssueTokenRequest issueTokenRequest
    ) {
        JwtAuthResult jwtAuthResult = authenticateUser.exec(
                AuthenticateUserArgument.of(issueTokenRequest.email(), issueTokenRequest.password())
        );

        // Refresh Token 쿠키 설정
        SafeCookie safeCookie = SafeCookie.of(
                REFRESH_TOKEN,
                jwtAuthResult.refreshToken().value(),
                jwtAuthResult.refreshTokenValidityInMills().toSecondsInt()
        );
        response.addCookie(safeCookie);

        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", jwtAuthResult.accessToken().value());

        return ResponseEntity.ok(data);
    }
}
