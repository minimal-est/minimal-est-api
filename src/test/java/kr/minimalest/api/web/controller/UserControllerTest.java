package kr.minimalest.api.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.minimalest.api.domain.access.exception.EmailDuplicatedException;
import kr.minimalest.api.application.user.SignUp;
import kr.minimalest.api.application.user.SignUpArgument;
import kr.minimalest.api.application.user.SignUpResult;
import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.web.controller.dto.request.SignUpRequest;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    SignUp signUp;

    @Nested
    @DisplayName("회원가입 API")
    class HappySignUp {

        @Getter
        private static class SignUpFixture {
            private final String emailStr = "test@test.com";
            private final String invalidEmailStr = "test@";

            private final String passwordStr = "test1234";
            private final String invalidPasswordStr = "p";

            private final String confirmPasswordStr = "test1234";
            private final String invalidConfirmPasswordStr = "tes123";

            public SignUpRequest getSignUpRequest() {
                return SignUpRequest.of(emailStr, passwordStr, confirmPasswordStr);
            }

            public SignUpRequest getInvalidSignUpRequest() {
                return SignUpRequest.of(invalidEmailStr, invalidPasswordStr, invalidConfirmPasswordStr);
            }

            public SignUpArgument getSignUpArgument() {
                return SignUpArgument.of(emailStr, passwordStr);
            }

            public SignUpResult generateSignUpResult() {
                return SignUpResult.of(UserId.of(UUID.randomUUID()));
            }
        }

        SignUpFixture fixture = new SignUpFixture();

        SignUpRequest request = fixture.getSignUpRequest();
        SignUpRequest invalidRequest = fixture.getInvalidSignUpRequest();

        SignUpArgument argument = fixture.getSignUpArgument();

        SignUpResult result = fixture.generateSignUpResult();

        @Test
        @DisplayName("정상적인 요청 시 회원가입 된 userUUID를 반환한다")
        void shouldReturnUserUUIDWhenRequestIsValid() throws Exception {
            // given
            given(signUp.exec(argument)).willReturn(result);

            // when
            ResultActions perform = mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userUUID").value(result.userId().id().toString()));
        }

        @Test
        @DisplayName("요청 형식이 올바르지 않을 시 400 에러를 반환한다")
        void shouldReturn400WhenRequestFormatIsInvalid() throws Exception {
            // when
            ResultActions perform = mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)));

            // then
            perform
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.properties.errors.email").exists())
                    .andExpect(jsonPath("$.properties.errors.password").exists())
                    .andExpect(jsonPath("$.properties.errors.confirmPassword").exists());
        }

        @Test
        @DisplayName("이메일 중복 시 409 에러를 반환한다")
        void shouldReturn409WhenEmailIsDuplicated() throws Exception {
            // given
            given(signUp.exec(argument)).willThrow(EmailDuplicatedException.class);

            // when
            ResultActions perform = mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform
                    .andExpect(status().isConflict());
        }
    }
}
