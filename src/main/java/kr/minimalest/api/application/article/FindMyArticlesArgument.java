package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.writing.ArticleStatus;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public record FindMyArticlesArgument(
        UUID blogId,
        String status,
        String searchKeyword,
        Pageable pageable
) {
}
