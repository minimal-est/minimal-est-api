package kr.minimalest.api.web;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;

public record Properties(@JsonValue Map<String, Object> map) {
    public static Properties of(String key, Object value) {
        return new Properties(Map.of(key, value));
    }

    public static Properties of(Map<String, Object> map) {
        return new Properties(map);
    }

    public static Properties ofEmpty() {
        return new Properties(Map.of());
    }
}
