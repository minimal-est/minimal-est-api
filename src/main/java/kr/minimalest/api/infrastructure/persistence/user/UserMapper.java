package kr.minimalest.api.infrastructure.persistence.user;

import kr.minimalest.api.domain.user.*;
import kr.minimalest.api.infrastructure.persistence.role.RoleEntity;

import java.util.stream.Collectors;

public class UserMapper {

    public static User toDomain(UserEntity userEntity) {
        if (userEntity == null) return null;
        return User.of(
                UserId.of(userEntity.getId()),
                UserUUID.of(userEntity.getUserUUID()),
                Email.of(userEntity.getEmail()),
                Password.of(userEntity.getPassword()),
                userEntity.getRoles().stream().map((re) ->
                        new Role(RoleId.of(re.getId()), re.getRoleType())
                ).collect(Collectors.toSet()),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }

    public static UserEntity toEntity(User user) {
        if (user == null) return null;
        return new UserEntity(
                user.userId() == null ? null : user.userId().value(),
                user.userUUID().value(),
                user.email().value(),
                user.password().value(),
                user.roles().stream().map((r) ->
                        new RoleEntity(r.roleId().value(), r.roleType())
                ).collect(Collectors.toSet()),
                user.createdAt(),
                user.updatedAt()
        );
    }
}
