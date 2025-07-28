package kr.minimalest.api;

import kr.minimalest.api.infrastructure.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
public class MinimalestApiV2Application {

    public static void main(String[] args) {
        SpringApplication.run(MinimalestApiV2Application.class, args);
    }

}
