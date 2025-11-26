package kr.minimalest.api.application.bookmark;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkCollectionRepository;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자의 모든 북마크 컬렉션 조회
 */
@Service
@RequiredArgsConstructor
public class FindUserBookmarkCollections {

    private final BookmarkCollectionRepository bookmarkCollectionRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional(readOnly = true)
    public List<BookmarkCollectionDetail> exec(UserId userId) {
        return bookmarkCollectionRepository.findByUserId(userId)
                .stream()
                .map(collection -> BookmarkCollectionDetail.from(
                        collection,
                        bookmarkRepository.countByCollectionId(collection.getId())
                ))
                .toList();
    }
}
