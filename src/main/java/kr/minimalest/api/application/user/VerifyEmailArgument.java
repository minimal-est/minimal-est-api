package kr.minimalest.api.application.user;

public record VerifyEmailArgument(
        String email,
        String token
) {
}
