package kr.minimalest.api.domain.user.repository;

import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.User;
import kr.minimalest.api.domain.user.UserId;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(UserId userId);

    Optional<User> findByEmail(Email email);

    UserId save(User user);
}
