package kr.minimalest.api.web.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record IssueTokenRequest(

        @Length(min = 2, max = 50)
        @Email
        @NotBlank
        String email,

        @Length(min = 2, max = 50)
        @NotBlank
        String password
) {
}
