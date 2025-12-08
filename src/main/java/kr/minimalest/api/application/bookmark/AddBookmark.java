package kr.minimalest.api.application.bookmark;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.Bookmark;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkAlreadyExistsException;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkAccessDeniedException;
import kr.minimalest.api.domain.discovery.bookmark.exception.BookmarkCollectionNotFoundException;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkCollectionRepository;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkRepository;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.Slug;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 북마크 추가 (글을 컬렉션에 저장)
 * - 같은 글을 여러 컬렉션에 저장 가능
 */
@Service
@RequiredArgsConstructor
public class AddBookmark {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkCollectionRepository bookmarkCollectionRepository;
    private final ArticleRepository articleRepository;
    private final BlogRepository blogRepository;

    @Transactional
    public BookmarkDetail exec(AddBookmarkArgument argument) {
        // 1. 글 존재 확인 (slug로 조회)
        Slug slugVO = new Slug(argument.slug());
        Article article = articleRepository.findBySlug(slugVO)
                .orElseThrow(() -> new IllegalArgumentException("아티클을 찾을 수 없습니다"));

        // 2. 컬렉션 존재 및 소유권 확인
        BookmarkCollection collection = bookmarkCollectionRepository.findById(argument.collectionId())
                .orElseThrow(() -> new BookmarkCollectionNotFoundException("컬렉션을 찾을 수 없습니다"));

        if (!collection.isOwnedBy(argument.userId())) {
            throw new BookmarkAccessDeniedException("컬렉션을 수정할 권한이 없습니다");
        }

        // 3. 중복 저장 확인 (같은 글을 같은 컬렉션에 여러 번 저장하지 않도록)
        if (bookmarkRepository.findByCollectionIdOrderBySequence(argument.collectionId())
                .stream()
                .anyMatch(b -> b.getArticleId().equals(article.getId()))) {
            throw new BookmarkAlreadyExistsException("이미 이 컬렉션에 저장된 글입니다");
        }

        // 4. 시퀀스 설정 (마지막 북마크 다음 번호)
        int maxSequence = bookmarkRepository.findMaxSequenceByCollectionId(argument.collectionId());
        int newSequence = maxSequence + 1;

        // 5. 북마크 생성 및 저장
        Bookmark bookmark = Bookmark.create(
                argument.collectionId(),
                article.getId(),
                newSequence
        );
        bookmarkRepository.save(bookmark);

        Blog blog = blogRepository.findById(article.getBlogId())
                .orElseThrow(() -> new IllegalArgumentException("블로그를 찾을 수 없습니다"));

        return BookmarkDetail.from(bookmark, article, blog);
    }

    public record AddBookmarkArgument(
            UserId userId,
            BookmarkCollectionId collectionId,
            String slug
    ) {}
}
