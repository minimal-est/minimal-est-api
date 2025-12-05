package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.engagement.reaction.ReactionType;
import kr.minimalest.api.domain.engagement.reaction.service.ArticleReactionService;
import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.service.BlogService;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleSummaryCreator {

    private final BlogService blogService;
    private final ArticleReactionService articleReactionService;

    // 도메인 -> DTO
    @Transactional(readOnly = true)
    public List<ArticleSummary> create(List<Article> articles) {
        if (articles.isEmpty()) {
            return List.of();
        }

        List<ArticleId> articleIds = articles.stream().map(Article::getId).toList();
        Map<ArticleId, Map<ReactionType, Long>> reactionCountMappings = articleReactionService.getReactionCountMappings(articleIds);

        Map<BlogId, Author> mappingAuthor = getMappingAuthorByArticles(articles);

        return articles.stream()
                .map(a -> ArticleSummary.from(
                        a,
                        mappingAuthor.get(a.getBlogId()),
                        reactionCountMappings.get(a.getId())
                )).toList();
    }

    // 도메인 -> DTO
    @Transactional(readOnly = true)
    public Page<ArticleSummary> createWithPage(Page<Article> articles) {
        if (articles.isEmpty()) {
            return Page.empty();
        }

        List<ArticleId> articleIds = articles.stream().map(Article::getId).toList();
        Map<ArticleId, Map<ReactionType, Long>> reactionCountMappings = articleReactionService.getReactionCountMappings(articleIds);

        Map<BlogId, Author> mappingAuthor = getMappingAuthorByArticles(articles);

        List<ArticleSummary> articleSummaries = articles.stream()
                .map(a -> ArticleSummary.from(
                        a,
                        mappingAuthor.get(a.getBlogId()),
                        reactionCountMappings.get(a.getId())
                )).toList();

        return new PageImpl<>(articleSummaries, articles.getPageable(), articles.getTotalElements());
    }

    private Map<BlogId, Author> getMappingAuthorByArticles(Iterable<Article> articles) {
        List<BlogId> blogIds = new ArrayList<>();
        articles.forEach(a -> blogIds.add(a.getBlogId()));
        return blogService.getMappingAuthor(blogIds);
    }
}
