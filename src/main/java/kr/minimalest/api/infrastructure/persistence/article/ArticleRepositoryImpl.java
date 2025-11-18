package kr.minimalest.api.infrastructure.persistence.article;

import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.ArticleStatus;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final SpringDataJpaArticleRepository springDataJpaArticleRepository;

    @Override
    public ArticleId save(Article article) {
        return springDataJpaArticleRepository.save(article).getId();
    }

    @Override
    public Optional<Article> findById(ArticleId articleId) {
        return springDataJpaArticleRepository.findById(articleId);
    }

    @Override
    public List<Article> findAllByIds(List<ArticleId> articleIds) {
        return springDataJpaArticleRepository.findAllById(articleIds);
    }

    public List<ArticleId> findTopNIdsByOrderByPublishedAtDesc(int page, int limit) {
        PageRequest pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "publishedAt"));
        return springDataJpaArticleRepository.findPublishedTopNIds(pageable);
    }

    @Override
    public Page<Article> findAllDraftedByBlogId(BlogId blogId, Pageable pageable) {
        return springDataJpaArticleRepository.findAllByStatusAndBlogId(ArticleStatus.DRAFT, blogId, pageable);
    }

    @Override
    public Page<Article> findAllCompletedByBlogId(BlogId blogId, Pageable pageable) {
        return springDataJpaArticleRepository.findAllByStatusAndBlogId(ArticleStatus.PUBLISHED, blogId, pageable);
    }

    @Override
    public Page<Article> findAllMyArticles(BlogId blogId, ArticleStatus status, String searchKeyword, Pageable pageable) {
        if (status != null && !searchKeyword.isEmpty()) {
            // 상태와 검색 키워드 모두 지정
            return springDataJpaArticleRepository.findAllByBlogIdAndStatusNotAndTitleContainingIgnoreCaseOrderByUpdatedAtDesc(
                    blogId, ArticleStatus.DELETED, searchKeyword, pageable);
        } else if (status != null) {
            // 상태만 지정 (DELETED 제외)
            if (status == ArticleStatus.DELETED) {
                throw new IllegalArgumentException("DELETED 상태는 조회할 수 없습니다.");
            }
            return springDataJpaArticleRepository.findAllByStatusAndBlogId(status, blogId, pageable);
        } else if (!searchKeyword.isEmpty()) {
            // 검색 키워드만 지정 (DELETED 제외)
            return springDataJpaArticleRepository.findAllByBlogIdAndStatusNotAndTitleContainingIgnoreCaseOrderByUpdatedAtDesc(
                    blogId, ArticleStatus.DELETED, searchKeyword, pageable);
        } else {
            // 조건 없음 (DELETED 제외하고 모든 글 조회)
            return springDataJpaArticleRepository.findAllByBlogIdAndStatusNotOrderByUpdatedAtDesc(
                    blogId, ArticleStatus.DELETED, pageable);
        }
    }
}
