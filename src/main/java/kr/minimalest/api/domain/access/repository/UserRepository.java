package kr.minimalest.api.domain.access.repository;

import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.User;
import kr.minimalest.api.domain.access.UserId;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(UserId userId);

    Optional<User> findByEmail(Email email);

    boolean existsByEmail(Email email);

    UserId save(User user);
}
