package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.article.Article;
import kr.minimalest.api.domain.article.ArticleId;
import kr.minimalest.api.domain.article.repository.ArticleRepository;
import kr.minimalest.api.domain.blog.BlogId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class CreateArticle {

    private final ArticleRepository articleRepository;
    private final BlogResolver blogResolver;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CreateArticleResult exec(CreateArticleArgument argument) {
        BlogId blogId = blogResolver.getBlogId(argument.userId());
        Article article = Article.create(blogId);
        ArticleId savedArticleId = articleRepository.save(article);

        publishEvent(article);

        return CreateArticleResult.of(savedArticleId);
    }

    private void publishEvent(Article article) {
        article.releaseEvents().forEach(eventPublisher::publishEvent);
    }
}
