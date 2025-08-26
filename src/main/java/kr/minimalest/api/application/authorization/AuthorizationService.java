package kr.minimalest.api.application.authorization;

import kr.minimalest.api.domain.article.ArticleId;
import kr.minimalest.api.domain.article.repository.ArticleRepository;
import kr.minimalest.api.domain.blog.BlogId;
import kr.minimalest.api.domain.blog.repository.BlogRepository;
import kr.minimalest.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final BlogRepository blogRepository;
    private final ArticleRepository articleRepository;

    public boolean userOwnsBlog(BlogId blogId, UserId userId) {
        return blogRepository.findById(blogId)
                .map(blog -> blog.isOwnedBy(userId))
                .orElse(false);
    }

    public boolean blogOwnsArticle(BlogId blogId, ArticleId articleId) {
        return articleRepository.findById(articleId)
                .map(article -> article.isOwnedBy(blogId))
                .orElse(false);
    }
}
