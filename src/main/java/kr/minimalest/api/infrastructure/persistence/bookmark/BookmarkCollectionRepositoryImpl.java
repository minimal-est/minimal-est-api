package kr.minimalest.api.infrastructure.persistence.bookmark;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.repository.BookmarkCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookmarkCollectionRepositoryImpl implements BookmarkCollectionRepository {

    private final JpaBookmarkCollectionRepository jpaRepository;

    @Override
    @Transactional
    public void save(BookmarkCollection collection) {
        jpaRepository.save(collection);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookmarkCollection> findById(BookmarkCollectionId id) {
        return jpaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookmarkCollection> findByUserId(UserId userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookmarkCollection> findDefaultByUserId(UserId userId) {
        return jpaRepository.findByUserIdAndIsDefaultTrue(userId);
    }

    @Override
    @Transactional
    public void delete(BookmarkCollection collection) {
        jpaRepository.delete(collection);
    }

    @Override
    @Transactional
    public void deleteById(BookmarkCollectionId id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByUserId(UserId userId) {
        jpaRepository.deleteByUserId(userId);
    }
}
