package kr.minimalest.api.application.bookmark;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.Bookmark;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkCollectionNotFoundException;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkAccessDeniedException;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkCollectionRepository;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 북마크 컬렉션 삭제
 * - 컬렉션 삭제 시 해당 북마크들을 사용자의 기본 컬렉션으로 이동
 */
@Service
@RequiredArgsConstructor
public class DeleteBookmarkCollection {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkCollectionRepository bookmarkCollectionRepository;

    @Transactional
    public void exec(DeleteBookmarkCollectionArgument argument) {
        // 1. 삭제할 컬렉션 조회
        BookmarkCollection collectionToDelete = bookmarkCollectionRepository.findById(argument.collectionId())
                .orElseThrow(() -> new BookmarkCollectionNotFoundException("컬렉션을 찾을 수 없습니다"));

        // 2. 소유권 확인
        if (!collectionToDelete.isOwnedBy(argument.userId())) {
            throw new BookmarkAccessDeniedException("컬렉션을 삭제할 권한이 없습니다");
        }

        // 3. 기본 컬렉션은 삭제 불가
        if (collectionToDelete.isDefault()) {
            throw new IllegalArgumentException("기본 컬렉션은 삭제할 수 없습니다");
        }

        // 4. 삭제 대상 컬렉션의 모든 북마크를 기본 컬렉션으로 이동
        List<Bookmark> bookmarksToMove = bookmarkRepository.findByCollectionIdOrderBySequence(argument.collectionId());

        if (!bookmarksToMove.isEmpty()) {
            BookmarkCollection defaultCollection = bookmarkCollectionRepository.findDefaultByUserId(argument.userId())
                    .orElseThrow(() -> new BookmarkCollectionNotFoundException("기본 컬렉션을 찾을 수 없습니다"));

            // 기본 컬렉션의 기존 articleId를 Set으로 조회 (1회만 호출)
            Set<kr.minimalest.api.domain.writing.ArticleId> existingArticleIds = bookmarkRepository
                    .findByCollectionIdOrderBySequence(defaultCollection.getId())
                    .stream()
                    .map(Bookmark::getArticleId)
                    .collect(Collectors.toSet());

            // 최대 시퀀스 한 번만 조회
            int maxSequence = bookmarkRepository.findMaxSequenceByCollectionId(defaultCollection.getId());

            for (Bookmark bookmark : bookmarksToMove) {
                // 기본 컬렉션에 없는 북마크만 이동
                if (!existingArticleIds.contains(bookmark.getArticleId())) {
                    bookmark.moveToCollection(defaultCollection.getId());
                    bookmark.updateSequence(++maxSequence);
                    bookmarkRepository.save(bookmark);
                }
            }
        }

        // 5. 컬렉션 삭제
        bookmarkCollectionRepository.delete(collectionToDelete);
    }

    public record DeleteBookmarkCollectionArgument(
            UserId userId,
            BookmarkCollectionId collectionId
    ) {}
}
