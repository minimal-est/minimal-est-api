package kr.minimalest.api.application.bookmark.listener;

import kr.minimalest.api.domain.access.event.UserSignedUpEvent;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkCollectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 가입 시 기본 북마크 컬렉션 자동 생성
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserSignedUpEventListener {

    private final BookmarkCollectionRepository bookmarkCollectionRepository;

    @EventListener
    @Transactional
    public void handleUserSignedUp(UserSignedUpEvent event) {
        log.info("사용자 가입 이벤트 수신: {}", event.getUserId());

        BookmarkCollection defaultCollection = BookmarkCollection.createDefault(event.getUserId());
        bookmarkCollectionRepository.save(defaultCollection);

        log.info("기본 북마크 컬렉션 생성 완료: userId={}", event.getUserId());
    }
}
