package kr.minimalest.api.infrastructure.service;

import kr.minimalest.api.application.article.BlogResolver;
import kr.minimalest.api.application.exception.UserHasNotBlogException;
import kr.minimalest.api.domain.blog.Blog;
import kr.minimalest.api.domain.blog.BlogId;
import kr.minimalest.api.domain.blog.repository.BlogRepository;
import kr.minimalest.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogResolverImpl implements BlogResolver {

    private final BlogRepository blogRepository;

    @Override
    public BlogId getBlogId(UserId userId) {
        Blog blog = blogRepository.findByUserId(userId)
                .orElseThrow(() -> new UserHasNotBlogException("사용자가 블로그를 개설하지 않았습니다."));
        return blog.getId();
    }
}
