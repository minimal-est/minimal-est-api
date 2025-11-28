package kr.minimalest.api.infrastructure.mail;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MailConfig {

    @Value("${mail.from-email:noreply@minimalest.kr}")
    private String fromEmail;

    @Value("${mail.verification.link-base-url:http://localhost:8080}")
    private String verificationLinkBaseUrl;

    @Value("${mail.verification.link-path:/api/v1/auth/verify}")
    private String verificationLinkPath;

    @Value("${mail.verification.subject:Minimalest 이메일 인증}")
    private String verificationSubject;

    @Value("${mail.verification.token-validity-hours:24}")
    private int tokenValidityHours;

    @Value("${redis.email-token-prefix:email:verification:}")
    private String emailTokenPrefix;

    public String getVerificationLink(String email, String token) {
        return verificationLinkBaseUrl + verificationLinkPath
            + "?email=" + email
            + "&token=" + token;
    }
}
