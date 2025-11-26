package kr.minimalest.api.application.bookmark;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkCollectionNotFoundException;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkAccessDeniedException;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkCollectionRepository;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 북마크 컬렉션 공개/비공개 토글
 */
@Service
@RequiredArgsConstructor
public class ToggleCollectionPublic {

    private final BookmarkCollectionRepository bookmarkCollectionRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public BookmarkCollectionDetail exec(ToggleCollectionPublicArgument argument) {
        // 1. 컬렉션 조회
        BookmarkCollection collection = bookmarkCollectionRepository.findById(argument.collectionId())
                .orElseThrow(() -> new BookmarkCollectionNotFoundException("컬렉션을 찾을 수 없습니다"));

        // 2. 소유권 확인
        if (!collection.isOwnedBy(argument.userId())) {
            throw new BookmarkAccessDeniedException("컬렉션을 수정할 권한이 없습니다");
        }

        // 3. 공개/비공개 토글
        collection.togglePublic();
        bookmarkCollectionRepository.save(collection);

        return BookmarkCollectionDetail.from(collection, bookmarkRepository.countByCollectionId(collection.getId()));
    }

    public record ToggleCollectionPublicArgument(
            UserId userId,
            BookmarkCollectionId collectionId
    ) {}
}
