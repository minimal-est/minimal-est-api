package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.minimalest.api.application.user.*;
import kr.minimalest.api.web.controller.dto.response.AccessTokenResponse;
import kr.minimalest.api.web.controller.dto.request.IssueTokenRequest;
import kr.minimalest.api.web.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "사용자 인증 관련 API")
public class AuthController {

    private final AuthenticateAndIssueToken authenticateAndIssueToken;

    private final AccessTokenReissue accessTokenReissue;

    private final String REFRESH_TOKEN = "refreshToken";

    @PostMapping("/token")
    @Operation(summary = "로그인 및 토큰 발급", description = "이메일과 패스워드를 사용해 로그인하고 액세스 토큰과 리프레시 토큰을 발급합니다.")
    public ResponseEntity<AccessTokenResponse> issueToken(
            @RequestBody @Valid IssueTokenRequest issueTokenRequest
    ) {
        AuthenticateAndIssueTokenResult authenticateAndIssueTokenResult = authenticateAndIssueToken.exec(
                AuthenticateAndIssueTokenArgument.of(issueTokenRequest.email(), issueTokenRequest.password())
        );

        ResponseCookie responseCookie = buildSafeRefreshResponseCookie(authenticateAndIssueTokenResult);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(AccessTokenResponse.of(authenticateAndIssueTokenResult.accessToken().value()));
    }

    private ResponseCookie buildSafeRefreshResponseCookie(AuthenticateAndIssueTokenResult authenticateAndIssueTokenResult) {
        return ResponseCookie
                .from(REFRESH_TOKEN, authenticateAndIssueTokenResult.refreshToken().value())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(authenticateAndIssueTokenResult.refreshTokenValidityInMills().toSeconds())
                .sameSite("lax")
                .build();
    }

    @PostMapping("/token/refresh")
    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰을 사용해 새로운 액세스 토큰을 발급합니다.")
    public ResponseEntity<AccessTokenResponse> refreshToken(
        @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new UnauthorizedException("리프레시 토큰 쿠키가 존재하지 않습니다.");
        }

        AccessTokenReissueResult accessTokenReissueResult = accessTokenReissue.exec(
                AccessTokenReissueArgument.of(refreshToken)
        );

        return ResponseEntity.ok()
                .body(AccessTokenResponse.of(accessTokenReissueResult.accessToken().value()));
    }
}
