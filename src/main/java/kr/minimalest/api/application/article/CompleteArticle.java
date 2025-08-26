package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.application.exception.ArticleNotFoundException;
import kr.minimalest.api.application.exception.ArticleCompleteFailException;
import kr.minimalest.api.domain.article.Article;
import kr.minimalest.api.domain.article.ArticleId;
import kr.minimalest.api.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Business
@RequiredArgsConstructor
public class CompleteArticle {

    private final ArticleRepository articleRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void exec(CompleteArticleArgument arg) {
        Article article = findArticle(arg.articleId());
        completeArticleAndPublishEvent(article);
    }

    private Article findArticle(ArticleId articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("해당 ID의 Article을 찾을 수 없습니다."));
    }

    private void completeArticleAndPublishEvent(Article article) {
        try {
            article.complete();
            publishEvent(article);
        } catch (IllegalArgumentException e) {
            log.error("글ID: {}", article.getId(), e);
            throw new ArticleCompleteFailException(e.getMessage(), e);
        } catch (Exception e) {
            log.error("글ID({}): {}", article.getStatus(), article.getId(), e);
            throw new ArticleCompleteFailException("글 완료 처리 중 오류가 발생했습니다.", e);
        }
    }

    private void publishEvent(Article article) {
        article.releaseEvents().forEach(eventPublisher::publishEvent);
    }
}
