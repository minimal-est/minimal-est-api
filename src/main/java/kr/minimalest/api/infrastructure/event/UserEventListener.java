package kr.minimalest.api.infrastructure.event;

import kr.minimalest.api.domain.access.event.UserSignedUpEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserSignedUp(UserSignedUpEvent event) {
        log.info("신규 사용자 등록: {}, {}", event.getUserId(), event.getEmail());
    }
}
