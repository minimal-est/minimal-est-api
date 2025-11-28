package kr.minimalest.api.domain.access.repository;

import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.PendingSignup;

import java.util.Optional;

public interface PendingSignupRepository {
    void save(PendingSignup pendingSignup);

    Optional<PendingSignup> findByEmail(Email email);

    void delete(Email email);
}
