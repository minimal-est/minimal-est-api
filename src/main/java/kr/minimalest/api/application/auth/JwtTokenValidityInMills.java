package kr.minimalest.api.application.auth;

public record JwtTokenValidityInMills(long value) {
    public JwtTokenValidityInMills {
        if (value <= 0) {
            throw new IllegalArgumentException("JwtTokenValidityInMills값은 0보다 커야 합니다!");
        }
    }

    public static JwtTokenValidityInMills of(long mills) {
        return new JwtTokenValidityInMills(mills);
    }

    public static JwtTokenValidityInMills ofSeconds(long seconds) {
        return new JwtTokenValidityInMills(seconds * 1000);
    }

    public int toSecondsInt() {
        long seconds = toSeconds();

        if (seconds > Integer.MAX_VALUE) {
            throw new IllegalStateException("Seconds가 int의 최대값보다 큽니다!");
        }
        return (int) seconds;
    }

    public long toSeconds() {
        return value / 1000;
    }
}
