package kr.minimalest.api.infrastructure.persistence.user;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.user.*;
import kr.minimalest.api.infrastructure.persistence.role.RoleEntity;
import kr.minimalest.api.infrastructure.persistence.role.RoleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(JpaUserRepository.class)
class JpaUserRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    JpaUserRepository userRepository;

    Role userRole;

    @BeforeEach
    void setUp() {
        RoleEntity userRoleEntity = new RoleEntity(null, RoleType.USER);
        em.persist(userRoleEntity);
        userRole = RoleMapper.toDomain(userRoleEntity);
    }

    @Test
    @Transactional
    void save_success() {
        // given
        UserUUID userUUID = UserUUID.of(UUID.randomUUID().toString());
        User user = User.withoutId(
                userUUID,
                Email.of("test@test.com"),
                Password.of("test"),
                Set.of(userRole),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when
        UserId savedUserId = userRepository.save(user);

        // then
        UserEntity userEntity = em.find(UserEntity.class, savedUserId.value());
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getUserUUID()).isEqualTo(userUUID.value());
    }

    @Test
    @Transactional
    void save_and_findById_success() {
        // given
        User user = User.withoutId(
                UserUUID.of(UUID.randomUUID().toString()),
                Email.of("test@test.com"),
                Password.of("test"),
                Set.of(userRole),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when
        UserId savedUserId = userRepository.save(user);
        Optional<User> findUser = userRepository.findById(savedUserId);

        // then
        assertThat(findUser.isPresent()).isTrue();
        assertThat(findUser.get().userId()).isEqualTo(savedUserId);
    }

    @Test
    @Transactional
    void save_and_findByUUID_success() {
        // given
        UserUUID userUUID = UserUUID.of(UUID.randomUUID().toString());
        User user = User.withoutId(
                userUUID,
                Email.of("test@test.com"),
                Password.of("test"),
                Set.of(userRole),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when
        UserId savedUserId = userRepository.save(user);
        Optional<User> findUser = userRepository.findByUUID(userUUID);

        // then
        assertThat(findUser.isPresent()).isTrue();
        assertThat(findUser.get().userId()).isEqualTo(savedUserId);
        assertThat(findUser.get().userUUID().value()).isEqualTo(userUUID.value());
    }

    @Test
    @Transactional
    void save_and_findByEmail_success() {
        // given
        Email email = Email.of("test@test.com");
        User user = User.withoutId(
                UserUUID.of(UUID.randomUUID().toString()),
                email,
                Password.of("test"),
                Set.of(userRole),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when
        UserId savedUserId = userRepository.save(user);
        Optional<User> findUser = userRepository.findByEmail(email);

        // then
        assertThat(findUser.isPresent()).isTrue();
        assertThat(findUser.get().userId()).isEqualTo(savedUserId);
        assertThat(findUser.get().email()).isEqualTo(email);
    }
}
