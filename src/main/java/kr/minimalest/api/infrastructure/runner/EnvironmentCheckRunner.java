package kr.minimalest.api.infrastructure.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EnvironmentCheckRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("Server started with environment: ");
        log.info("MAIL_VERIFICATION_LINK_BASE_URL: {}", System.getenv("MAIL_VERIFICATION_LINK_BASE_URL"));
    }
}
