package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleStatus;
import kr.minimalest.api.domain.writing.Content;
import kr.minimalest.api.domain.writing.Description;
import kr.minimalest.api.domain.writing.Title;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Application 계층에서 Web 계층으로
 * 상세 글 정보를 전달하기 위한 DTO
 */
public record ArticleDetail(
        UUID articleId,
        String title,
        String content,
        String description,
        ArticleStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime publishedAt,
        AuthorInfo author
) {
    public static ArticleDetail from(Article article, Author author) {
        String title = Optional.ofNullable(article.getTitle())
                .map(Title::value)
                .orElse("");

        String content = Optional.ofNullable(article.getContent())
                .map(Content::value)
                .orElse("");

        String description = Optional.ofNullable(article.getDescription())
                .map(Description::value)
                .orElse("");

        return new ArticleDetail(
                article.getRawId(),
                title,
                content,
                description,
                article.getStatus(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getPublishedAt(),
                new AuthorInfo(
                        author.getId().id(),
                        author.getPenName().value()
                )
        );
    }
}
