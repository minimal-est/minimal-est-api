package kr.minimalest.api.web;

public record ErrorResponse (
        Status status,
        Title title,
        Detail detail,
        Properties properties
){
    public ErrorResponse {}

    public static ErrorResponse of(Status status, Title title, Detail detail, Properties properties) {
        return new ErrorResponse(status, title, detail, properties);
    }

    public static ErrorResponse of(Status status, Title title, Detail detail) {
        return new ErrorResponse(status, title, detail, Properties.ofEmpty());
    }
}
