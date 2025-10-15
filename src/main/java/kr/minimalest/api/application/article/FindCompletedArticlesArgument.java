package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.publishing.BlogId;
import org.springframework.data.domain.Pageable;

public record FindCompletedArticlesArgument(BlogId blogId, Pageable pageable) {
    public FindCompletedArticlesArgument {

    }
}
