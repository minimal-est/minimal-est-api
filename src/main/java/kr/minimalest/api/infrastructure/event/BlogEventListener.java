package kr.minimalest.api.infrastructure.event;

import kr.minimalest.api.domain.publishing.event.CreatedBlogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogEventListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreatedBlog(CreatedBlogEvent event) {
        log.info("새로운 블로그 생성: {}, {}", event.getBlogId(), event.getPenName());
    }
}
