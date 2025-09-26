package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class CreateArticle {

    private final ArticleRepository articleRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CreateArticleResult exec(CreateArticleArgument argument) {
        Article article = Article.create(argument.blogId());
        ArticleId savedArticleId = articleRepository.save(article);

        publishEvent(article);

        return CreateArticleResult.of(savedArticleId);
    }

    private void publishEvent(Article article) {
        article.releaseEvents().forEach(eventPublisher::publishEvent);
    }
}
