package kr.minimalest.api.application.bookmark;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.Bookmark;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkId;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkNotFoundException;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkAccessDeniedException;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkCollectionNotFoundException;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkAlreadyExistsException;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkCollectionRepository;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkRepository;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 북마크를 다른 컬렉션으로 이동
 */
@Service
@RequiredArgsConstructor
public class MoveBookmark {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkCollectionRepository bookmarkCollectionRepository;
    private final ArticleRepository articleRepository;
    private final BlogRepository blogRepository;

    @Transactional
    public BookmarkDetail exec(MoveBookmarkArgument argument) {
        // 1. 북마크 조회
        Bookmark bookmark = bookmarkRepository.findById(argument.bookmarkId())
                .orElseThrow(() -> new BookmarkNotFoundException("북마크를 찾을 수 없습니다"));

        // 2. 현재 컬렉션 조회 및 소유권 확인
        BookmarkCollection currentCollection = bookmarkCollectionRepository.findById(bookmark.getCollectionId())
                .orElseThrow(() -> new BookmarkCollectionNotFoundException("현재 컬렉션을 찾을 수 없습니다"));

        if (!currentCollection.isOwnedBy(argument.userId())) {
            throw new BookmarkAccessDeniedException("북마크를 이동할 권한이 없습니다");
        }

        // 3. 대상 컬렉션 조회 및 소유권 확인
        BookmarkCollection targetCollection = bookmarkCollectionRepository.findById(argument.toCollectionId())
                .orElseThrow(() -> new BookmarkCollectionNotFoundException("대상 컬렉션을 찾을 수 없습니다"));

        if (!targetCollection.isOwnedBy(argument.userId())) {
            throw new BookmarkAccessDeniedException("대상 컬렉션을 수정할 권한이 없습니다");
        }

        // 4. 대상 컬렉션에 같은 글이 이미 있는지 확인
        if (bookmarkRepository.findByCollectionIdOrderBySequence(argument.toCollectionId())
                .stream()
                .anyMatch(b -> b.getArticleId().equals(bookmark.getArticleId()))) {
            throw new BookmarkAlreadyExistsException("대상 컬렉션에 이미 같은 글이 저장되어 있습니다");
        }

        // 5. 원래 컬렉션 ID 저장 (이동 전에 저장)
        BookmarkCollectionId originalCollectionId = bookmark.getCollectionId();

        // 6. 북마크를 새 컬렉션으로 이동
        int maxSequence = bookmarkRepository.findMaxSequenceByCollectionId(argument.toCollectionId());
        bookmark.moveToCollection(argument.toCollectionId());
        bookmark.updateSequence(maxSequence + 1);
        bookmarkRepository.save(bookmark);

        // 7. 원래 컬렉션에서 시퀀스 재정렬
        bookmarkRepository.compactSequences(originalCollectionId);

        Article article = articleRepository.findById(bookmark.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("아티클을 찾을 수 없습니다"));

        Blog blog = blogRepository.findById(article.getBlogId())
                .orElseThrow(() -> new IllegalArgumentException("블로그를 찾을 수 없습니다"));

        return BookmarkDetail.from(bookmark, article, blog);
    }

    public record MoveBookmarkArgument(
            UserId userId,
            BookmarkId bookmarkId,
            BookmarkCollectionId toCollectionId
    ) {}
}
