package kr.minimalest.api.application.blog;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.exception.BlogNotFoundException;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class FindBlog {

    private final BlogRepository blogRepository;

    public FindBlogResult exec(FindBlogArgument argument) {
        Blog blog = blogRepository.findByPenName(PenName.of(argument.penName()))
                .orElseThrow(() -> new BlogNotFoundException("해당 펜네임 블로그가 존재하지 않습니다: " + argument.penName()));

        BlogInfo blogInfo = BlogInfo.of(blog);

        return new FindBlogResult(blogInfo);
    }
}
