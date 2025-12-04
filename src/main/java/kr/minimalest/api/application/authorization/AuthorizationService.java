package kr.minimalest.api.application.authorization;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final BlogRepository blogRepository;
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public boolean userOwnsBlog(BlogId blogId, UserId userId) {
        return blogRepository.findById(blogId)
                .map(blog -> blog.isOwnedBy(userId))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean blogOwnsArticle(BlogId blogId, ArticleId articleId) {
        return articleRepository.findById(articleId)
                .map(article -> article.isOwnedBy(blogId))
                .orElse(false);
    }
}
