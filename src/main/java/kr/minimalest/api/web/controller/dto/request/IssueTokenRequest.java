package kr.minimalest.api.web.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IssueTokenRequest(

        @NotBlank
        @Size(min = 2, max = 50)
        @Email
        String email,

        @NotBlank
        @Size(min = 2, max = 50)
        String password
) {
}
