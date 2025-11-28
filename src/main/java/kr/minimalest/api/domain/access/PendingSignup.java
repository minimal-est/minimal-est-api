package kr.minimalest.api.domain.access;

public record PendingSignup(
    Email email,
    Password encryptedPassword
) {
    public static PendingSignup of(Email email, Password encryptedPassword) {
        return new PendingSignup(email, encryptedPassword);
    }
}
