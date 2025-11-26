package kr.minimalest.api.application.bookmark;

import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkCollectionNotFoundException;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkCollectionRepository;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 공개된 북마크 컬렉션 조회 (인증 불필요)
 */
@Service
@RequiredArgsConstructor
public class FindPublicBookmarkCollection {

    private final BookmarkCollectionRepository bookmarkCollectionRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional(readOnly = true)
    public BookmarkCollectionDetail exec(UUID collectionId) {
        BookmarkCollectionId id = BookmarkCollectionId.of(collectionId);
        BookmarkCollection collection = bookmarkCollectionRepository.findById(id)
                .orElseThrow(() -> new BookmarkCollectionNotFoundException(
                        "북마크 컬렉션을 찾을 수 없습니다: " + collectionId
                ));

        if (!collection.isPublic()) {
            throw new BookmarkCollectionNotFoundException(
                    "이 컬렉션은 비공개 컬렉션입니다."
            );
        }

        return BookmarkCollectionDetail.from(collection, bookmarkRepository.countByCollectionId(id));
    }
}
