package kr.minimalest.api.domain.user;

public record TokenValidityInMills(long value) {
    public TokenValidityInMills {
        if (value <= 0) {
            throw new IllegalArgumentException("TokenValidityInMills값은 0보다 커야 합니다!");
        }
    }

    public static TokenValidityInMills of(long mills) {
        return new TokenValidityInMills(mills);
    }

    public static TokenValidityInMills ofSeconds(long seconds) {
        return new TokenValidityInMills(seconds * 1000);
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

