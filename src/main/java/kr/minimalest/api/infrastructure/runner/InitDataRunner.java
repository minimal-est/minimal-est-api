package kr.minimalest.api.infrastructure.runner;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.Password;
import kr.minimalest.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("default")
@RequiredArgsConstructor
@Slf4j
public class InitDataRunner implements CommandLineRunner {

    private final EntityManager em;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void run(String... args) {

        User user = User.signUp(
                Email.of("user@test.com"),
                Password.of(encoder.encode("user1234"))
        );

        User admin = User.signUp(
                Email.of("admin@test.com"),
                Password.of(encoder.encode("admin1234"))
        );

        em.persist(user);
        em.persist(admin);

        admin.assignAdmin();

        log.info("초기 데이터 삽입 완료: Roles, Admin User, Normal User");
    }
}
