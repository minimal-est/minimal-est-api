package kr.minimalest.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MinimalestApiV2ApplicationTests {

    @Test
    @DisplayName("정상적으로 Spring Context가 Load 된다.")
    void contextLoads() {
    }

}
