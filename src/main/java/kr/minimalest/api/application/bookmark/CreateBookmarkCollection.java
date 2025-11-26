package kr.minimalest.api.application.bookmark;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkCollectionRepository;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 북마크 컬렉션 생성
 */
@Service
@RequiredArgsConstructor
public class CreateBookmarkCollection {

    private final BookmarkCollectionRepository bookmarkCollectionRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public BookmarkCollectionDetail exec(CreateBookmarkCollectionArgument argument) {
        BookmarkCollection collection = BookmarkCollection.create(
                argument.userId(),
                argument.title(),
                argument.description()
        );
        bookmarkCollectionRepository.save(collection);
        return BookmarkCollectionDetail.from(collection, bookmarkRepository.countByCollectionId(collection.getId()));
    }

    public record CreateBookmarkCollectionArgument(
            UserId userId,
            String title,
            String description
    ) {}
}
