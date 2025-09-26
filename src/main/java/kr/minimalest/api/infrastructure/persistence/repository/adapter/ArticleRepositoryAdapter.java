package kr.minimalest.api.infrastructure.persistence.repository.adapter;

import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.ArticleStatus;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import kr.minimalest.api.infrastructure.persistence.repository.SpringDataJpaArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ArticleRepositoryAdapter implements ArticleRepository {

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

    @Override
    public List<ArticleId> findTopNIdsByOrderByCompletedAtDesc(int page, int limit) {
        PageRequest pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "completedAt"));
        return springDataJpaArticleRepository.findTopNIds(pageable);
    }

    @Override
    public List<Article> findAllDraftedByBlogId(BlogId blogId, Pageable pageable) {
        return springDataJpaArticleRepository.findAllByStatusAndBlogId(ArticleStatus.DRAFT, blogId, pageable).getContent();
    }
}
