package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.minimalest.api.application.user.*;
import kr.minimalest.api.infrastructure.security.JwtUserDetails;
import kr.minimalest.api.web.controller.dto.request.SignUpRequest;
import kr.minimalest.api.web.controller.dto.request.VerifyEmailResponse;
import kr.minimalest.api.web.controller.dto.response.AccessTokenResponse;
import kr.minimalest.api.web.controller.dto.response.CurrentUserResponse;
import kr.minimalest.api.web.controller.dto.request.IssueTokenRequest;
import kr.minimalest.api.web.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    private final SignUp signUp;

    private final VerifyEmail verifyEmail;

    private final String REFRESH_TOKEN = "refreshToken";

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "현재 사용자 조회",
            description = "JWT 토큰으로 인증된 현재 사용자의 ID를 조회합니다. (인증 필수)"
    )
    public ResponseEntity<CurrentUserResponse> getCurrentUser(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        return ResponseEntity.ok(CurrentUserResponse.of(
                jwtUserDetails.getUserId().id()
        ));
    }

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

    @PostMapping("/signup")
    @Operation(
            summary = "이메일 인증 회원가입",
            description = "이메일, 비밀번호로 회원가입을 신청하고 인증 이메일을 발송합니다."
    )
    public ResponseEntity<?> signupWithEmailVerification(
            @RequestBody @Valid SignUpRequest request
    ) {
        signUp.exec(
                SignUpArgument.of(request.email(), request.password())
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify")
    @Operation(
            summary = "이메일 인증",
            description = "인증 링크의 토큰으로 이메일을 인증합니다."
    )
    public ResponseEntity<?> verifyEmail(
            @RequestParam String email,
            @RequestParam String token
    ) {
        try {
            verifyEmail.exec(new VerifyEmailArgument(email, token));
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "http://localhost:5173/auth/email-verified")
                    .build();
        } catch (Exception e) {
            log.error("이메일 인증 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "http//localhost:5173/auth/verify-failed")
                    .build();
        }
    }
}
