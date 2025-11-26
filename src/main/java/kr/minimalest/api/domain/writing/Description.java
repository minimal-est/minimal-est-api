package kr.minimalest.api.domain.writing;

public record Description(String value) {
    public static Description empty() {
        return new Description("");
    }

    public int length() {
        return value.length();
    }

    public Description trim() {
        return new Description(value.trim());
    }
}
