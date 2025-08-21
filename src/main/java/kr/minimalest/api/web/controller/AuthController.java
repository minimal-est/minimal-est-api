package kr.minimalest.api.web.controller;

import jakarta.validation.Valid;
import kr.minimalest.api.application.user.*;
import kr.minimalest.api.web.controller.dto.AccessTokenResponse;
import kr.minimalest.api.web.controller.dto.IssueTokenRequest;
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
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateAndIssueToken authenticateAndIssueToken;

    private final AccessTokenReissue accessTokenReissue;

    private final String REFRESH_TOKEN = "refreshToken";

    @PostMapping("/token")
    public ResponseEntity<?> issueToken(
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
    public ResponseEntity<?> refreshToken(
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
