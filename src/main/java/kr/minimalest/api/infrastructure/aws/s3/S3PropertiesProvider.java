package kr.minimalest.api.infrastructure.aws.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cloud.aws.s3")
public record S3PropertiesProvider(
    String bucket
) { }
