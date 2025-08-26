package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.application.exception.ArticleNotFoundException;
import kr.minimalest.api.application.exception.ArticleStateException;
import kr.minimalest.api.domain.article.Article;
import kr.minimalest.api.domain.article.ArticleId;
import kr.minimalest.api.domain.article.Content;
import kr.minimalest.api.domain.article.Title;
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
        Title title = Title.of(arg.title());
        Content content = Content.of(arg.content());

        updateArticleAndPublishEvent(article, title, content);
    }

    private void updateArticleAndPublishEvent(Article article, Title title, Content content) {
        try {
            article.update(title, content);
            publishEvent(article);
        } catch (Exception e) {
            throw new ArticleStateException("글을 수정할 수 없는 상태입니다.", e);
        }
    }

    private Article findArticle(ArticleId articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("해당 ID의 Article을 찾을 수 없습니다."));
    }

    private void publishEvent(Article article) {
        article.releaseEvents().forEach(eventPublisher::publishEvent);
    }
}
