package kr.minimalest.api.infrastructure.persistence.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.User;
import kr.minimalest.api.domain.user.UserId;
import kr.minimalest.api.domain.user.UserUUID;
import kr.minimalest.api.domain.user.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId userId) {
        UserEntity userEntity = em.find(UserEntity.class, userId.value());
        return Optional.ofNullable(UserMapper.toDomain(userEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUUID(UserUUID userUUID) {
        UserEntity userEntity = em
                .createQuery("SELECT u FROM UserEntity u WHERE u.userUUID = :uuid", UserEntity.class)
                .setParameter("uuid", userUUID.value())
                .getResultStream()
                .findFirst()
                .orElse(null);
        return Optional.ofNullable(UserMapper.toDomain(userEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        UserEntity userEntity = em
                .createQuery("SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class)
                .setParameter("email", email.value())
                .getResultStream()
                .findFirst()
                .orElse(null);
        return Optional.ofNullable(UserMapper.toDomain(userEntity));
    }

    @Override
    @Transactional
    public UserId save(User user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        em.persist(userEntity);
        return UserId.of(userEntity.getId());
    }
}
