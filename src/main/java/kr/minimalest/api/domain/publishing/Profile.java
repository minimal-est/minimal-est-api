package kr.minimalest.api.domain.publishing;

import org.springframework.util.Assert;

import java.util.UUID;
import java.util.regex.Pattern;

public record Profile(
        String url
) {

    public static Profile random(UUID seed) {
        return new Profile("https://api.dicebear.com/9.x/thumbs/svg?" + seed);
    }
}
