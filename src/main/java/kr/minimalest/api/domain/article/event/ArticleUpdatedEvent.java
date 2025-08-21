package kr.minimalest.api.domain.article.event;

import kr.minimalest.api.domain.DomainEvent;
import kr.minimalest.api.domain.article.ArticleId;

public class ArticleUpdatedEvent extends DomainEvent {

    private final ArticleId articleId;

    private ArticleUpdatedEvent(ArticleId articleId) {
        this.articleId = articleId;
    }

    public static ArticleUpdatedEvent of(ArticleId articleId) {
        return new ArticleUpdatedEvent(articleId);
    }
}
