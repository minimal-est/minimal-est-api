package kr.minimalest.api.infrastructure.mail;

import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.VerificationToken;
import kr.minimalest.api.domain.access.exception.EmailSendForVerificationFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class SesAsyncEmailSender {
    private final SesClient sesClient;
    private final MailConfig mailConfig;

    @Async
    public void sendVerificationEmail(Email email, VerificationToken token) {
        try {
            String verificationLink = mailConfig.getVerificationLink(email.value(), token.uuid().toString());
            String htmlBody = buildVerificationEmailHtml(verificationLink);

            Destination destination = Destination.builder()
                    .toAddresses(email.value())
                    .build();

            Content subject = Content.builder()
                    .data(mailConfig.getVerificationSubject())
                    .charset("UTF-8")
                    .build();

            Content htmlContent = Content.builder()
                    .data(htmlBody)
                    .charset("UTF-8")
                    .build();

            Body body = Body.builder()
                    .html(htmlContent)
                    .build();

            Message message = Message.builder()
                    .subject(subject)
                    .body(body)
                    .build();

            SendEmailRequest request = SendEmailRequest.builder()
                    .source(mailConfig.getFromEmail())
                    .destination(destination)
                    .message(message)
                    .configurationSetName("minimalest")
                    .build();

            SendEmailResponse response = sesClient.sendEmail(request);
            log.info("SES 인증 이메일 발송 성공: {} (MessageId: {})", email.value(), response.messageId());
        } catch (Exception e) {
            log.error("SES 인증 이메일 발송 실패: {}, 에러: {}", email.value(), e.getMessage());
            throw new EmailSendForVerificationFailedException("이메일 발송에 실패했습니다.", e);
        }
    }

    private String buildVerificationEmailHtml(String verificationLink) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px;}
                        .header { background-color: #f5f5f5; padding: 20px; border-radius: 5px; }
                        .content { padding: 20px 0; }
                        .button { background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; }
                        .footer { font-size: 12px; color: #999; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h2>Minimalest 이메일 인증</h2>
                        </div>
                        <div class="content">
                            <p>안녕하세요,</p>
                            <p>다음 버튼을 클릭하여 이메일을 인증하세요.</p>
                            <p><a href="%s" class="button">이메일 인증하기</a></p>
                            <p>또는 다음 링크를 복사하여 브라우저에 붙여넣으세요:</p>
                            <p><code>%s</code></p>
                            <p>이 링크는 %d시간 동안 유효합니다.</p>
                        </div>
                        <div class="footer">
                            <p>Minimalest Team</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(verificationLink, verificationLink, mailConfig.getTokenValidityHours());
    }
}
