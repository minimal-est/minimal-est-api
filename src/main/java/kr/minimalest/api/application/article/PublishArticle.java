package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.writing.exception.ArticleNotFoundException;
import kr.minimalest.api.domain.writing.exception.ArticleCompleteFailException;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Business
@RequiredArgsConstructor
public class PublishArticle {

    private final ArticleRepository articleRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void exec(PublishArticleArgument arg) {
        Article article = findArticle(arg.articleId());
        publishArticleAndPublishEvent(article);
    }

    private Article findArticle(ArticleId articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("해당 ID의 Article을 찾을 수 없습니다."));
    }

    private void publishArticleAndPublishEvent(Article article) {
        try {
            article.publish();
            publishEvent(article);
        } catch (IllegalArgumentException e) {
            throw new ArticleCompleteFailException(e.getMessage(), e);
        } catch (Exception e) {
            log.error("", e);
            throw new ArticleCompleteFailException("글 완료 처리 중 오류가 발생했습니다.", e);
        }
    }

    private void publishEvent(Article article) {
        article.releaseEvents().forEach(eventPublisher::publishEvent);
    }
}
