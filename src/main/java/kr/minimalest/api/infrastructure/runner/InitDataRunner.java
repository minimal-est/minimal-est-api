package kr.minimalest.api.infrastructure.runner;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.user.RoleType;
import kr.minimalest.api.infrastructure.persistence.role.RoleEntity;
import kr.minimalest.api.infrastructure.persistence.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Component
@Profile("local")
@RequiredArgsConstructor
@Slf4j
public class InitDataRunner implements CommandLineRunner {

    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        RoleEntity userRole = new RoleEntity(null, RoleType.USER);
        RoleEntity adminRole = new RoleEntity(null, RoleType.ADMIN);

        em.persist(userRole);
        em.persist(adminRole);

        UserEntity adminUser = new UserEntity(
                null,
                UUID.randomUUID().toString(),
                "admin@test.com",
                passwordEncoder.encode("admin1234"),
                Set.of(adminRole),  // 관리자 권한
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        em.persist(adminUser);

        UserEntity normalUser = new UserEntity(
                null,
                UUID.randomUUID().toString(),
                "user@test.com",
                passwordEncoder.encode("user1234"),
                Set.of(userRole),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        em.persist(normalUser);

        log.info("초기 데이터 삽입 완료: Roles, Admin User, Normal User");
    }
}
