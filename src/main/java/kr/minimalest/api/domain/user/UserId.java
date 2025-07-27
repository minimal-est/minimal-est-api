package kr.minimalest.api.domain.user;

public record UserId (Long value){

        public UserId {
                if (value == null)
                        throw new IllegalArgumentException("userId는 null이 될 수 없습니다!");

                if (value <= 0)
                        throw new IllegalArgumentException("userId는 0보다 커야 합니다!");
        }

        public static UserId of(Long value) {
                return new UserId(value);
        }
}
