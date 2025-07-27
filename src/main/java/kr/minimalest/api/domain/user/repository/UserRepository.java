package kr.minimalest.api.domain.user.repository;

import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.User;
import kr.minimalest.api.domain.user.UserId;
import kr.minimalest.api.domain.user.UserUUID;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(UserId userId);

    Optional<User> findByUUID(UserUUID userUUID);

    Optional<User> findByEmail(Email email);

    UserId save(User user);
}
