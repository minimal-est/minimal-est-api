package kr.minimalest.api.web.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.minimalest.api.web.validator.PasswordMatches;
import org.springframework.util.Assert;

@PasswordMatches
public record SignUpRequest (

        @NotBlank
        @Size(min = 5, max = 100)
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 100)
        String password,

        @NotBlank
        @Size(min = 8, max = 100)
        String confirmPassword
) {
        public SignUpRequest {
                Assert.hasText(email, "email은 공백일 수 없습니다.");
                Assert.hasText(password, "password는 공백일 수 없습니다.");
                Assert.hasText(confirmPassword, "confirmPassword는 공백일 수 없습니다.");
        }

        public static SignUpRequest of(String email, String password, String confirmPassword) {
                return new SignUpRequest(email, password, confirmPassword);
        }
}
