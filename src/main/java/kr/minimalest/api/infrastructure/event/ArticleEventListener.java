package kr.minimalest.api.infrastructure.event;

import kr.minimalest.api.domain.article.event.ArticleCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ArticleEventListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onArticleCreated(ArticleCreatedEvent event) {
        log.info("새로운 글 작성이 시작되었습니다: {}, {}", event.getBlogId(), event.getArticleId());
    }
}
