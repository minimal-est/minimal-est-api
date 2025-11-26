package kr.minimalest.api.application.bookmark;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.Bookmark;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkId;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkNotFoundException;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkAccessDeniedException;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkCollectionNotFoundException;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkCollectionRepository;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 북마크 순서 변경 (드래그 정렬)
 */
@Service
@RequiredArgsConstructor
public class ReorderBookmarks {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkCollectionRepository bookmarkCollectionRepository;

    @Transactional
    public void exec(ReorderBookmarksArgument argument) {
        // 1. 컬렉션 조회 및 소유권 확인
        BookmarkCollection collection = bookmarkCollectionRepository.findById(argument.collectionId())
                .orElseThrow(() -> new BookmarkCollectionNotFoundException("컬렉션을 찾을 수 없습니다"));

        if (!collection.isOwnedBy(argument.userId())) {
            throw new BookmarkAccessDeniedException("컬렉션을 수정할 권한이 없습니다");
        }

        // 2. 컬렉션의 모든 북마크 조회
        List<Bookmark> bookmarks = bookmarkRepository.findByCollectionIdOrderBySequence(argument.collectionId());

        // 3. bookmarkId -> Bookmark 맵 생성
        Map<BookmarkId, Bookmark> bookmarkMap = bookmarks.stream()
                .collect(Collectors.toMap(Bookmark::getId, b -> b));

        // 4. 새로운 순서대로 시퀀스 업데이트
        for (int i = 0; i < argument.bookmarkIds().size(); i++) {
            BookmarkId bookmarkId = argument.bookmarkIds().get(i);
            Bookmark bookmark = bookmarkMap.get(bookmarkId);

            if (bookmark == null) {
                throw new BookmarkNotFoundException("북마크를 찾을 수 없습니다: " + bookmarkId);
            }

            // 같은 컬렉션의 북마크인지 확인
            if (!bookmark.getCollectionId().equals(argument.collectionId())) {
                throw new IllegalArgumentException("다른 컬렉션의 북마크가 포함되어 있습니다");
            }

            bookmark.updateSequence(i);
            bookmarkRepository.save(bookmark);
        }
    }

    public record ReorderBookmarksArgument(
            UserId userId,
            BookmarkCollectionId collectionId,
            List<BookmarkId> bookmarkIds
    ) {}
}
