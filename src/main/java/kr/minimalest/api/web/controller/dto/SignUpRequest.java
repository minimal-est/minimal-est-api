package kr.minimalest.api.web.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.minimalest.api.web.validator.PasswordMatches;

@PasswordMatches
public record SignUpRequest (

        @NotBlank
        @Size(min = 2, max = 50)
        @Email
        String email,

        @NotBlank
        @Size(min = 2)
        String password,

        @NotBlank
        @Size(min = 2)
        String confirmPassword
) { }
