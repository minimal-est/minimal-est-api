package kr.minimalest.api.application.blog;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.exception.BlogNotFoundException;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class FindProfileImageUrl {

    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    public FindProfileImageUrlResult exec(FindProfileImageUrlArgument argument) {
        BlogId blogId = BlogId.of(argument.blogId());
        Author author = blogRepository.findAuthorById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("해당 블로그를 찾을 수 없습니다: " + blogId.id()));

        String profileImageUrl = author.getProfile().url();

        return new FindProfileImageUrlResult(profileImageUrl);
    }
}
