package kr.minimalest.api.application.blog;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.exception.BlogNotFoundException;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class FindBlogDetails {

    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    public FindBlogDetailsResult exec(FindBlogDetailsArgument arg) {
        Blog blog = blogRepository.findByPenName(PenName.of(arg.penName()))
                .orElseThrow(() -> new BlogNotFoundException("해당 블로그는 존재하지 않습니다: " + arg.penName()));

        return new FindBlogDetailsResult(BlogDetails.of(blog));
    }
}
