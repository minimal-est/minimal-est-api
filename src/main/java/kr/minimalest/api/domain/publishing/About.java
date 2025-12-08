package kr.minimalest.api.domain.publishing;

public record About(String value) {
    public static About empty() {
        return new About("");
    }
}
