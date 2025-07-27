package kr.minimalest.api.domain.user;

public record Role(

        RoleId roleId,

        RoleType roleType
) {
    public static Role of(
            RoleId roleId,
            RoleType roleType
    ) {
        return new Role(roleId, roleType);
    }
}
