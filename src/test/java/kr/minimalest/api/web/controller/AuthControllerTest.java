package kr.minimalest.api.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import kr.minimalest.api.application.user.AccessTokenReissue;
import kr.minimalest.api.application.user.AccessTokenReissueResult;
import kr.minimalest.api.application.user.AuthenticateAndIssueToken;
import kr.minimalest.api.application.user.AuthenticateAndIssueTokenResult;
import kr.minimalest.api.domain.user.Token;
import kr.minimalest.api.domain.user.TokenValidityInMills;
import kr.minimalest.api.web.controller.dto.request.IssueTokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AuthenticateAndIssueToken authenticateAndIssueToken;

    @MockitoBean
    AccessTokenReissue accessTokenReissue;

    @Nested
    @DisplayName("토큰 발급 API")
    class IssueToken {

        @Test
        @DisplayName("정상적인 요청시 토큰을 발급한다")
        void shouldIssueTokenWhenValidRequest() throws Exception {
            // given
            IssueTokenRequest issueTokenRequest = new IssueTokenRequest("test@test.com", "test1234");
            AuthenticateAndIssueTokenResult authResult = new AuthenticateAndIssueTokenResult(
                    Token.of("access-token-value"),
                    Token.of("refresh-token-value"),
                    TokenValidityInMills.ofSeconds(3600)
            );

            given(authenticateAndIssueToken.exec(any()))
                    .willReturn(authResult);

            // when
            ResultActions perform = mockMvc.perform(post("/api/v1/auth/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(issueTokenRequest)));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(cookie().exists("refreshToken"))
                    .andExpect(cookie().httpOnly("refreshToken", true))
                    .andExpect(cookie().secure("refreshToken", true))
                    .andExpect(jsonPath("$.accessToken").value("access-token-value"));
        }

        @Test
        @DisplayName("잘못된 이메일 형식으로 요청시 400 에러를 반환한다")
        void shouldReturn400WhenInvalidEmail() throws Exception {
            // given
            IssueTokenRequest request = new IssueTokenRequest("test@", "test1234");

            // when
            ResultActions perform = mockMvc.perform(post("/api/v1/auth/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("토큰 갱신 API")
    class RefreshToken {

        @Test
        @DisplayName("유효한 리프레시 토큰으로 액세스 토큰을 갱신한다")
        void shouldRefreshAccessTokenWithValidRefreshToken() throws Exception {
            // given
            AccessTokenReissueResult result = new AccessTokenReissueResult(
                    Token.of("new-access-token")
            );

            given(accessTokenReissue.exec(any()))
                    .willReturn(result);

            // when
            ResultActions perform = mockMvc.perform(post("/api/v1/auth/token/refresh")
                    .cookie(new Cookie("refreshToken", "valid-refresh-token")));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("new-access-token"));
        }

        @Test
        @DisplayName("리프레시 토큰이 없는 경우 401 에러를 반환한다")
        void shouldReturn401WhenNoRefreshToken() throws Exception {
            // when
            ResultActions perform = mockMvc.perform(post("/api/v1/auth/token/refresh"));

            // then
            perform.andExpect(status().isUnauthorized());
        }
    }
}
