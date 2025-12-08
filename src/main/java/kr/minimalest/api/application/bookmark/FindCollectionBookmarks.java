package kr.minimalest.api.application.bookmark;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.Bookmark;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkCollectionRepository;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkRepository;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 특정 컬렉션의 모든 북마크 조회 (순서대로)
 */
@Service
@RequiredArgsConstructor
public class FindCollectionBookmarks {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkCollectionRepository bookmarkCollectionRepository;
    private final ArticleRepository articleRepository;
    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    public CollectionBookmarksDetail exec(FindCollectionBookmarksArgument argument) {
        // 1. 컬렉션 조회
        BookmarkCollection collection = bookmarkCollectionRepository.findById(argument.collectionId())
                .orElseThrow(() -> new IllegalArgumentException("컬렉션을 찾을 수 없습니다"));

        // 2. 공개 컬렉션이거나 소유자인 경우에만 조회 가능
        if (!collection.isPublic() && !collection.isOwnedBy(argument.userId())) {
            throw new IllegalArgumentException("컬렉션을 조회할 권한이 없습니다");
        }

        // 3. 컬렉션의 모든 북마크 조회 (시퀀스 순)
        List<BookmarkDetail> bookmarks = bookmarkRepository.findByCollectionIdOrderBySequence(argument.collectionId())
                .stream()
                .map(bookmark -> {
                    Article article = articleRepository.findById(bookmark.getArticleId())
                            .orElseThrow(() -> new IllegalArgumentException("아티클을 찾을 수 없습니다"));
                    Blog blog = blogRepository.findById(article.getBlogId())
                            .orElseThrow(() -> new IllegalArgumentException("블로그를 찾을 수 없습니다"));
                    return BookmarkDetail.from(bookmark, article, blog);
                })
                .toList();

        // 4. 북마크 개수 조회
        int bookmarkCount = bookmarkRepository.countByCollectionId(argument.collectionId());

        return CollectionBookmarksDetail.of(bookmarkCount, bookmarks);
    }

    public record FindCollectionBookmarksArgument(
            UserId userId,
            BookmarkCollectionId collectionId
    ) {}
}
