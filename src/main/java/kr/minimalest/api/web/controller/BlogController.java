package kr.minimalest.api.web.controller;

import jakarta.validation.Valid;
import kr.minimalest.api.application.article.*;
import kr.minimalest.api.application.blog.CreateBlog;
import kr.minimalest.api.application.blog.CreateBlogArgument;
import kr.minimalest.api.application.blog.CreateBlogResult;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.infrastructure.security.JwtUserDetails;
import kr.minimalest.api.web.controller.dto.request.CreateBlogRequest;
import kr.minimalest.api.web.controller.dto.request.UpdateArticleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blogs")
public class BlogController {

    private final CreateBlog createBlog;

    private final CreateArticle createArticle;
    private final UpdateArticle updateArticle;
    private final CompleteArticle completeArticle;

    private final FindDraftArticles findDraftArticles;

    @PostMapping
    public ResponseEntity<?> createBlog(
            @RequestBody @Valid CreateBlogRequest createBlogRequest,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        CreateBlogArgument argument = CreateBlogArgument.of(jwtUserDetails.getUserId(), createBlogRequest.penName());
        CreateBlogResult result = createBlog.exec(argument);
        return ResponseEntity.ok(Map.of("blogId", result.blogId().id()));
    }

    @PostMapping("{blogId}/articles")
    @PreAuthorize("@authorizationService.userOwnsBlog(#blogId, #jwtUserDetails.userId)")
    public ResponseEntity<?> createArticle(
            @PathVariable UUID blogId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        CreateArticleArgument argument = CreateArticleArgument.of(BlogId.of(blogId));
        CreateArticleResult result = createArticle.exec(argument);
        return ResponseEntity.ok(Map.of("articleId", result.articleId().id()));
    }

    @PutMapping("{blogId}/articles/{articleId}")
    @PreAuthorize(
            """
            @authorizationService.userOwnsBlog(#blogId, #jwtUserDetails.userId) and
            @authorizationService.blogOwnsArticle(#blogId, #articleId)
            """
    )
    public ResponseEntity<?> updateArticle(
            @PathVariable UUID blogId,
            @PathVariable UUID articleId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @Valid @RequestBody UpdateArticleRequest request
    ) {
        UpdateArticleArgument argument = UpdateArticleArgument.of(
                ArticleId.of(articleId), request.title(), request.content()
        );
        updateArticle.exec(argument);
        return ResponseEntity.ok(
                Map.of(
                        "articleId", articleId,
                        "title", request.title()
                ));
    }

    @PostMapping("{blogId}/articles/{articleId}/complete")
    @PreAuthorize(
            """
            @authorizationService.userOwnsBlog(#blogId, #jwtUserDetails.userId) and
            @authorizationService.blogOwnsArticle(#blogId, #articleId)
            """
    )
    public ResponseEntity<?> completeArticle(
            @PathVariable UUID blogId,
            @PathVariable UUID articleId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        CompleteArticleArgument argument = CompleteArticleArgument.of(ArticleId.of(articleId));
        completeArticle.exec(argument);
        return ResponseEntity.ok(
                Map.of("articleId", articleId)
        );
    }

    @GetMapping("/{blogId}/articles/completed")
    public ResponseEntity<?> findCompletedArticles() {
        return null;
    }

    @PreAuthorize("#authorizationService.userOwnsBlog(#blogId, #jwtUserDetails.userId)")
    @GetMapping("/{blogId}/articles/draft")
    public ResponseEntity<?> findDraftArticles(
            @PathVariable UUID blogId,
            @PageableDefault(sort = "updated_at", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        FindDraftArticlesArgument argument = new FindDraftArticlesArgument(BlogId.of(blogId), pageable);
        FindRecommendArticlesResult result = findDraftArticles.exec(argument);
        return ResponseEntity.ok(Map.of("articleSummaries", result));
    }
}
