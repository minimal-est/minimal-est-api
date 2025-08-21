package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.blog.BlogId;
import kr.minimalest.api.domain.user.UserId;

// anti-corruption layer
public interface BlogResolver {

    BlogId getBlogId(UserId userId);
}
