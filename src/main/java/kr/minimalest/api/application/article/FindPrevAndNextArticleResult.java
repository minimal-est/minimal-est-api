package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.writing.Article;

public record FindPrevAndNextArticleResult(
        ArticleSummary prevArticle,
        ArticleSummary nextArticle
) {
    public static FindPrevAndNextArticleResult of(Article prevArticle, Article nextArticle, Author author) {
        ArticleSummary prevSummary = prevArticle == null ? null : ArticleSummary.from(prevArticle, author);
        ArticleSummary nextSummary = nextArticle == null ? null : ArticleSummary.from(nextArticle, author);
        return new FindPrevAndNextArticleResult(prevSummary, nextSummary);
    }
}
