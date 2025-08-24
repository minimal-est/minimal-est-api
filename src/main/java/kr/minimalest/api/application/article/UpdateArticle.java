package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.application.exception.ArticleNotFoundException;
import kr.minimalest.api.domain.article.Article;
import kr.minimalest.api.domain.article.ArticleId;
import kr.minimalest.api.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class UpdateArticle {

    private final ArticleRepository articleRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void exec(UpdateArticleArgument arg) {
        Article article = findArticle(arg.articleId());
        article.update(arg.title(), arg.content());
        publishEvent(article);
    }

    private Article findArticle(ArticleId articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("해당 ID의 Article을 찾을 수 없습니다."));
    }

    private void publishEvent(Article article) {
        article.releaseEvents().forEach(eventPublisher::publishEvent);
    }
}
