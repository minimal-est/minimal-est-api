package kr.minimalest.api.infrastructure.persistence.bookmark;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.Bookmark;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkId;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepository {

    private final JpaBookmarkRepository jpaRepository;
    private final EntityManager em;

    @Override
    @Transactional
    public void save(Bookmark bookmark) {
        jpaRepository.save(bookmark);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bookmark> findById(BookmarkId id) {
        return jpaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bookmark> findByCollectionIdOrderBySequence(BookmarkCollectionId collectionId) {
        return jpaRepository.findByCollectionIdOrderBySequenceAsc(collectionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bookmark> findByArticleId(ArticleId articleId) {
        return jpaRepository.findByArticleId(articleId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndArticleId(UserId userId, ArticleId articleId) {
        Long result = (Long) em.createQuery(
                "SELECT COUNT(b) FROM Bookmark b " +
                "JOIN BookmarkCollection bc ON b.collectionId = bc.id " +
                "WHERE bc.userId = :userId AND b.articleId = :articleId"
        )
                .setParameter("userId", userId)
                .setParameter("articleId", articleId)
                .getSingleResult();
        return result != null && result > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bookmark> findByUserIdAndArticleId(UserId userId, ArticleId articleId) {
        try {
            Bookmark bookmark = (Bookmark) em.createQuery(
                    "SELECT b FROM Bookmark b " +
                    "JOIN BookmarkCollection bc ON b.collectionId = bc.id " +
                    "WHERE bc.userId = :userId AND b.articleId = :articleId"
            )
                    .setParameter("userId", userId)
                    .setParameter("articleId", articleId)
                    .getSingleResult();
            return Optional.of(bookmark);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int findMaxSequenceByCollectionId(BookmarkCollectionId collectionId) {
        return jpaRepository.findMaxSequenceByCollectionId(collectionId);
    }

    @Override
    @Transactional
    public void delete(Bookmark bookmark) {
        jpaRepository.delete(bookmark);
    }

    @Override
    @Transactional
    public void deleteById(BookmarkId id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByCollectionId(BookmarkCollectionId collectionId) {
        jpaRepository.deleteByCollectionId(collectionId);
    }

    @Override
    @Transactional
    public void deleteByArticleId(ArticleId articleId) {
        jpaRepository.deleteByArticleId(articleId);
    }

    @Override
    @Transactional
    public void compactSequences(BookmarkCollectionId collectionId) {
        // 애플리케이션 레벨에서 시퀀스 재정렬 (MySQL의 UPDATE 제약 우회)
        List<Bookmark> bookmarks = jpaRepository.findByCollectionIdOrderBySequenceAsc(collectionId);

        // 0부터 순차적으로 시퀀스 재할당
        for (int i = 0; i < bookmarks.size(); i++) {
            bookmarks.get(i).updateSequence(i);
        }

        // 벌크 저장
        jpaRepository.saveAll(bookmarks);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByCollectionId(BookmarkCollectionId collectionId) {
        return jpaRepository.countByCollectionId(collectionId);
    }
}
