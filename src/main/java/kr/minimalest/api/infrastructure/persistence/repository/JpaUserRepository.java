package kr.minimalest.api.infrastructure.persistence.repository;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.User;
import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.access.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    private final EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId userId) {
        User user = em.find(User.class, userId);
        return Optional.ofNullable(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        User user = em
                .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
        return Optional.ofNullable(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(Email email) {
        Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

    @Override
    @Transactional
    public UserId save(User user) {
        em.persist(user);
        return user.getId();
    }
}
