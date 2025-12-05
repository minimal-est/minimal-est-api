package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.engagement.reaction.service.ArticleReactionService;
import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.writing.Article;

public record FindPrevAndNextArticleResult(
        ArticleSummary prevArticle,
        ArticleSummary nextArticle
) {
    public static FindPrevAndNextArticleResult of(Article prevArticle, Article nextArticle, Author author, ArticleReactionService articleReactionService) {
        ArticleSummary prevSummary = prevArticle == null ? null : ArticleSummary.from(prevArticle, author, articleReactionService.getReactionCounts(prevArticle.getId()));
        ArticleSummary nextSummary = nextArticle == null ? null : ArticleSummary.from(nextArticle, author, articleReactionService.getReactionCounts(nextArticle.getId()));
        return new FindPrevAndNextArticleResult(prevSummary, nextSummary);
    }
}
