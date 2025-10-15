package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindCompletedArticlesTest {

    @InjectMocks
    private FindCompletedArticles findCompletedArticles;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleSummaryCreator articleSummaryCreator;

    @Test
    @DisplayName("정상적으로 결과 반환")
    void shouldReturnResults() {
        // given
        int totalElements = 10;
        int pageNumber = 1;
        int pageSize = 3;
        BlogId blogId = BlogId.generate();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        FindCompletedArticlesArgument argument = new FindCompletedArticlesArgument(blogId, pageable);

        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < totalElements; i++) {
            articles.add(Article.create(blogId));
        }

        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, articles.size());
        List<Article> pagedArticles = articles.subList(fromIndex, toIndex);
        Page<Article> articlesPage = new PageImpl<>(pagedArticles, pageable, articles.size());

        List<ArticleSummary> expectedSummaries = pagedArticles.stream()
                .map(a -> ArticleSummary.from(a, PenName.of("test")))
                .toList();

        Page<ArticleSummary> summariesPage = new PageImpl<>(expectedSummaries, pageable, articles.size());

        given(articleRepository.findAllCompletedByBlogId(blogId, pageable)).willReturn(articlesPage);
        given(articleSummaryCreator.createWithPage(eq(articlesPage))).willReturn(summariesPage);

        // when
        FindCompletedArticlesResult result = findCompletedArticles.exec(argument);

        // then
        assertThat(result.articleSummaries().getTotalPages()).isEqualTo((int)Math.ceil((double) totalElements / pageSize));
        assertThat(result.articleSummaries().getNumberOfElements()).isEqualTo(pageSize);
        assertThat(result.articleSummaries().getTotalElements()).isEqualTo(totalElements);
    }
}
