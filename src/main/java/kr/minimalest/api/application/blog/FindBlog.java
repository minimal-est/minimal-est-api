package kr.minimalest.api.application.blog;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.exception.BlogNotFoundException;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class FindBlog {

    private final BlogRepository blogRepository;

    public FindBlogResult exec(FindBlogArgument argument) {
        Blog blog = blogRepository.findByUserId(argument.userId())
                .orElseThrow(() -> new BlogNotFoundException("블로그가 존재하지 않습니다."));

        return new FindBlogResult(blog.getId(), argument.userId(), blog.getPenName());
    }
}
