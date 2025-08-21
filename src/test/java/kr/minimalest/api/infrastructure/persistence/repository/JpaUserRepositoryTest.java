package kr.minimalest.api.infrastructure.persistence.repository;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.Password;
import kr.minimalest.api.domain.user.User;
import kr.minimalest.api.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("사용자 저장 성공 시 UserId가 반환된다")
    void shouldReturnUserIdWhenUserIsSaved() {
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
    @DisplayName("사용자 ID로 사용자를 조회한다")
    void shouldReturnUserWhenFindingById() {
        // given
        User user = User.signUp(Email.of("test@test.com"), Password.of("test1234"));
        UserId savedUserId = userRepository.save(user);

        // when
        Optional<User> findUser = userRepository.findById(savedUserId);

        // then
        assertThat(findUser.isPresent()).isTrue();
        assertThat(findUser.get().getId()).isEqualTo(savedUserId);
    }

    @Test
    @Transactional
    @DisplayName("사용자 이메일로 사용자를 조회한다")
    void shouldReturnUserWhenFindingByEmail() {
        // given
        Email email = Email.of("test@test.com");
        User user = User.signUp(Email.of("test@test.com"), Password.of("test1234"));
        UserId savedUserId = userRepository.save(user);

        // when
        Optional<User> findUser = userRepository.findByEmail(email);

        // then
        assertThat(findUser.isPresent()).isTrue();
        assertThat(findUser.get().getId()).isEqualTo(savedUserId);
        assertThat(findUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    @Transactional
    @DisplayName("사용자 이메일로 사용자 존재 유무를 조회하고, 존재하면 True를 반환한다")
    void shouldReturnTrueWhenEmailExists() {
        // given
        Email email = Email.of("test@test.com");
        User user = User.signUp(Email.of("test@test.com"), Password.of("test1234"));
        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByEmail(email);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("사용자 이메일로 사용자 존재 유무를 조회하고, 존재하지 않으면 False를 반환한다")
    void shouldReturnFalseWhenEmailNotExists() {
        // given
        Email email = Email.of("test@test.com");

        // when
        boolean exists = userRepository.existsByEmail(email);

        // then
        assertThat(exists).isFalse();
    }
}
