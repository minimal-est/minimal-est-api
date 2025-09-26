package kr.minimalest.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ConfigurationPropertiesScan
public class MinimalestApiV2Application {

    public static void main(String[] args) {
        SpringApplication.run(MinimalestApiV2Application.class, args);
    }

}
