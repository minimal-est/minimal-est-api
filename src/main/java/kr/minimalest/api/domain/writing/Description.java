package kr.minimalest.api.domain.writing;

public record Description(String value) {
    public static Description empty() {
        return new Description("");
    }
}
