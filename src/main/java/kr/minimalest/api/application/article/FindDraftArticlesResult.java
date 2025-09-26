package kr.minimalest.api.application.article;

import java.util.List;

public record FindDraftArticlesResult(List<ArticleSummary> articleSummaries) {
}
