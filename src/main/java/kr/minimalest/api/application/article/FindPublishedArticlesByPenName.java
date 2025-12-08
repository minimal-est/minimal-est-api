package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.exception.BlogNotFoundException;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@RequiredArgsConstructor
public class FindPublishedArticlesByPenName {

    private final BlogRepository blogRepository;
    private final ArticleRepository articleRepository;
    private final ArticleSummaryCreator articleSummaryCreator;

    @Transactional(readOnly = true)
    public FindPublishedArticlesByPenNameResult exec(FindPublishedArticlesByPenNameArgument argument) {
        // 1. penName으로 Blog 조회
        PenName penName = new PenName(argument.penName());
        Blog blog = blogRepository.findByPenName(penName)
                .orElseThrow(() -> new BlogNotFoundException("블로그를 찾을 수 없습니다."));

        // 2. 최신순 정렬로 Pageable 생성
        PageRequest pageable = PageRequest.of(
                argument.page(),
                argument.size(),
                Sort.by(Sort.Direction.DESC, "publishedAt")
        );

        // 3. 발행된 글 조회
        Page<Article> articles = articleRepository.findAllCompletedByBlogId(blog.getId(), pageable);

        // 4. ArticleSummary로 변환
        Page<ArticleSummary> summaries = articleSummaryCreator.createWithPage(articles);

        return new FindPublishedArticlesByPenNameResult(
                summaries.getContent(),
                articles.getTotalElements(),
                articles.getTotalPages(),
                articles.getNumber(),
                articles.getSize()
        );
    }

    public record FindPublishedArticlesByPenNameArgument(
            String penName,
            int page,
            int size
    ) {
        public static FindPublishedArticlesByPenNameArgument of(String penName, int page, int size) {
            return new FindPublishedArticlesByPenNameArgument(penName, page, size);
        }
    }

    public record FindPublishedArticlesByPenNameResult(
            List<ArticleSummary> articles,
            long totalElements,
            int totalPages,
            int currentPage,
            int pageSize
    ) {}
}
