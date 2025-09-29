package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.service.BlogService;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindDraftArticlesTest {

    @InjectMocks
    private FindDraftArticles findDraftArticles;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private BlogService blogService;

    @Test
    @DisplayName("정상적으로 결과 반환")
    void shouldReturnResults() {
        // given
        BlogId blogId = BlogId.generate();
        Pageable pageable = PageRequest.of(0, 2);
        FindDraftArticlesArgument argument = new FindDraftArticlesArgument(blogId, pageable);

        List<BlogId> blogIds = List.of(blogId);
        Map<BlogId, PenName> penNameMap = Map.of(blogId, PenName.of("Tester"));

        List<Article> expectedArticles = List.of(
                Article.create(blogId),
                Article.create(blogId),
                Article.create(blogId),
                Article.create(blogId)
        );

        given(articleRepository.findAllDraftedByBlogId(blogId, pageable)).willReturn(expectedArticles);
        given(blogService.getMappingPenNames(any(List.class))).willReturn(penNameMap);

        // when
        FindRecommendArticlesResult result = findDraftArticles.exec(argument);

        // then
        assertThat(result.articleSummaries().size()).isEqualTo(4);
    }
}
