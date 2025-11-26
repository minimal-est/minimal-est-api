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

/**
 * 북마크 제거
 * - 북마크 삭제 후 시퀀스 재정렬
 */
@Service
@RequiredArgsConstructor
public class RemoveBookmark {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkCollectionRepository bookmarkCollectionRepository;

    @Transactional
    public void exec(RemoveBookmarkArgument argument) {
        // 1. 북마크 조회
        Bookmark bookmark = bookmarkRepository.findById(argument.bookmarkId())
                .orElseThrow(() -> new BookmarkNotFoundException("북마크를 찾을 수 없습니다"));

        // 2. 컬렉션 조회 및 소유권 확인
        BookmarkCollection collection = bookmarkCollectionRepository.findById(bookmark.getCollectionId())
                .orElseThrow(() -> new BookmarkCollectionNotFoundException("컬렉션을 찾을 수 없습니다"));

        if (!collection.isOwnedBy(argument.userId())) {
            throw new BookmarkAccessDeniedException("북마크를 삭제할 권한이 없습니다");
        }

        // 3. 북마크 삭제
        bookmarkRepository.delete(bookmark);

        // 4. 시퀀스 재정렬 (갭 제거)
        bookmarkRepository.compactSequences(bookmark.getCollectionId());
    }

    public record RemoveBookmarkArgument(
            UserId userId,
            BookmarkId bookmarkId
    ) {}
}
