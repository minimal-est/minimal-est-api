package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.service.BlogService;
import kr.minimalest.api.domain.writing.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ArticleSummaryCreator {

    private final BlogService blogService;

    // 도메인 -> DTO
    public List<ArticleSummary> create(List<Article> articles) {
        if (articles.isEmpty()) {
            return List.of();
        }

        Map<BlogId, Author> mappingAuthor = getMappingAuthorByArticles(articles);

        return articles.stream()
                .map(a -> ArticleSummary.from(a, mappingAuthor.get(a.getBlogId())))
                .toList();
    }

    // 도메인 -> DTO
    public Page<ArticleSummary> createWithPage(Page<Article> articles) {
        if (articles.isEmpty()) {
            return Page.empty();
        }

        Map<BlogId, Author> mappingAuthor = getMappingAuthorByArticles(articles);

        List<ArticleSummary> articleSummaries = articles.stream()
                .map(a -> ArticleSummary.from(a, mappingAuthor.get(a.getBlogId())))
                .toList();

        return new PageImpl<>(articleSummaries, articles.getPageable(), articles.getTotalElements());
    }

    private Map<BlogId, Author> getMappingAuthorByArticles(Iterable<Article> articles) {
        List<BlogId> blogIds = new ArrayList<>();
        articles.forEach(a -> blogIds.add(a.getBlogId()));
        return blogService.getMappingAuthor(blogIds);
    }
}
