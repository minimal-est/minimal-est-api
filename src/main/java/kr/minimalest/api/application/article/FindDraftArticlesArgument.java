package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.publishing.BlogId;
import org.springframework.data.domain.Pageable;

public record FindDraftArticlesArgument(BlogId blogId, Pageable pageable) {

}
