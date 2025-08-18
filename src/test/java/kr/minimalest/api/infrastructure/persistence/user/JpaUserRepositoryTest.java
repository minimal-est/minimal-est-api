package kr.minimalest.api.infrastructure.persistence.user;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.user.*;
import kr.minimalest.api.infrastructure.persistence.repository.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(JpaUserRepository.class)
class JpaUserRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    JpaUserRepository userRepository;

    @Test
    @Transactional
    void save_success() {
        // given
        User user = User.signUp(Email.of("test@test.com"), Password.of("test1234"));

        // when
        UserId savedUserId = userRepository.save(user);

        // then
        User userEntity = em.find(User.class, savedUserId);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getId()).isEqualTo(user.getId());
    }

    @Test
    @Transactional
    void save_and_findById_success() {
        // given
        User user = User.signUp(Email.of("test@test.com"), Password.of("test1234"));

        // when
        UserId savedUserId = userRepository.save(user);
        Optional<User> findUser = userRepository.findById(savedUserId);

        // then
        assertThat(findUser.isPresent()).isTrue();
        assertThat(findUser.get().getId()).isEqualTo(savedUserId);
    }

    @Test
    @Transactional
    void save_and_findByEmail_success() {
        // given
        Email email = Email.of("test@test.com");
        User user = User.signUp(Email.of("test@test.com"), Password.of("test1234"));

        // when
        UserId savedUserId = userRepository.save(user);
        Optional<User> findUser = userRepository.findByEmail(email);

        // then
        assertThat(findUser.isPresent()).isTrue();
        assertThat(findUser.get().getId()).isEqualTo(savedUserId);
        assertThat(findUser.get().getEmail()).isEqualTo(email);
    }
}
