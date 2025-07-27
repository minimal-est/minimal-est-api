package kr.minimalest.api.infrastructure.persistence.role;

import kr.minimalest.api.domain.user.Role;
import kr.minimalest.api.domain.user.RoleId;

public class RoleMapper {

    public static Role toDomain(RoleEntity roleEntity) {
        if (roleEntity == null) return null;
        return Role.of(
                RoleId.of(roleEntity.getId()),
                roleEntity.getRoleType()
        );
    }

    public static RoleEntity toEntity(Role role) {
        if (role == null) return null;
        return new RoleEntity(role.roleId().value(), role.roleType());
    }
}
