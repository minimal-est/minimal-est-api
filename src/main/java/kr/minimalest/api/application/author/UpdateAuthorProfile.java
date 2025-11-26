package kr.minimalest.api.application.author;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.publishing.exception.AuthorNotFoundException;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 저자 프로필 이미지 URL 업데이트 Use Case
 *
 * 프로필 이미지가 S3에 업로드된 후, URL을 저장하는 Use Case
 */
@Component
@RequiredArgsConstructor
public class UpdateAuthorProfile {

    private final BlogRepository blogRepository;

    @Transactional
    public UpdateAuthorProfileResult exec(UpdateAuthorProfileArgument argument) {
        UserId userId = UserId.of(argument.userId());

        // 1. Author 조회
        var author = blogRepository.findAuthorByUserId(userId)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found for userId: " + userId));

        // 2. 프로필 URL 검증
        validateProfileUrl(argument.profileImageUrl());

        // 3. 프로필 URL 업데이트
        author.updateProfileUrl(argument.profileImageUrl());

        // 4. 저장
        blogRepository.saveAuthor(author);

        // 5. 결과 반환
        return UpdateAuthorProfileResult.of(author);
    }

    private void validateProfileUrl(String profileImageUrl) {
        // URL 형식 검증 (S3 URL만 허용)
        if (profileImageUrl == null || profileImageUrl.isBlank()) {
            throw new IllegalArgumentException("프로필 이미지 URL이 필수입니다.");
        }

        if (!isValidS3Url(profileImageUrl)) {
            throw new IllegalArgumentException("S3 URL 형식이 아닙니다.");
        }
    }

    private boolean isValidS3Url(String url) {
        // S3 URL 패턴: https://{bucket}.s3.{region}.amazonaws.com/{key}
        // 또는 https://s3.{region}.amazonaws.com/{bucket}/{key}
        // 또는 CloudFront URL, S3 경로 스타일 등
        return url.startsWith("https://") &&
               (url.contains(".s3") || url.contains("amazonaws.com") || url.contains("cloudfront.net"));
    }
}
