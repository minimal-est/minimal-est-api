package kr.minimalest.api.domain.article.event;

import kr.minimalest.api.domain.DomainEvent;
import kr.minimalest.api.domain.article.ArticleId;

public class ArticleCompletedEvent extends DomainEvent {

    private final ArticleId articleId;

    private ArticleCompletedEvent(ArticleId articleId) {
        this.articleId = articleId;
    }

    public static ArticleCompletedEvent of(ArticleId articleId) {
        return new ArticleCompletedEvent(articleId);
    }
}
