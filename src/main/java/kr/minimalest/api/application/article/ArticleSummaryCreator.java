package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.service.BlogService;
import kr.minimalest.api.domain.writing.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArticleSummaryCreator {

    private final BlogService blogService;

    public List<ArticleSummary> create(List<Article> articles) {
        if (articles.isEmpty()) {
            return List.of();
        }

        Set<BlogId> uniqueBlogIds = articles.stream()
                .map(Article::getBlogId)
                .collect(Collectors.toSet());

        Map<BlogId, PenName> penNameMap = blogService.getMappingPenNames(uniqueBlogIds);

        return articles.stream()
                .map(article -> ArticleSummary.from(article, penNameMap.get(article.getBlogId())))
                .toList();
    }

    public Page<ArticleSummary> createWithPage(Page<Article> articles) {
        if (articles.isEmpty()) {
            return Page.empty();
        }

        Set<BlogId> uniqueBlogIds = articles.stream()
                .map(Article::getBlogId)
                .collect(Collectors.toSet());

        Map<BlogId, PenName> penNameMap = blogService.getMappingPenNames(uniqueBlogIds);

        List<ArticleSummary> articleSummaries = articles.stream()
                .map(article -> ArticleSummary.from(article, penNameMap.get(article.getBlogId())))
                .toList();

        return new PageImpl<>(articleSummaries, articles.getPageable(), articles.getTotalElements());
    }
}
