package kr.minimalest.api.domain.writing;

public record Title(String value) {

    public Title {}

    public static Title of(String value) {
        return new Title(value);
    }

    public static Title empty() {
        return new Title("");
    }

    public Title trim() {
        return new Title(value.trim());
    }

    public int length() {
        return value.length();
    }
}
