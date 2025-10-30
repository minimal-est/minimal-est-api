package kr.minimalest.api.web.controller;

import jakarta.validation.Valid;
import kr.minimalest.api.application.article.*;
import kr.minimalest.api.application.blog.*;
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

    private final FindBlog findBlog;
    private final FindBlogSelf findBlogSelf;
    private final CreateBlog createBlog;

    private final CreateArticle createArticle;
    private final UpdateArticle updateArticle;
    private final CompleteArticle completeArticle;

    private final FindDraftArticles findDraftArticles;
    private final FindCompletedArticles findCompletedArticles;

    // 현재 로그인한 사용자의 BlogId를 반환합니다.
    @GetMapping("self")
    public ResponseEntity<?> findBlogSelf(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        FindBlogSelfArgument argument = new FindBlogSelfArgument(jwtUserDetails.getUserId());
        FindBlogSelfResult result = findBlogSelf.exec(argument);
        return ResponseEntity.ok(Map.of(
                "blogId", result.blogId().id(),
                "userId", result.userId().id(),
                "penName", result.penName().value()
        ));
    }

    @GetMapping("{penName}")
    public ResponseEntity<?> findBlog(
            @PathVariable String penName
    ) {
        FindBlogArgument argument = new FindBlogArgument(penName);
        FindBlogResult result = findBlog.exec(argument);
        return ResponseEntity.ok(Map.of(
                "blogId", result.blogId().id(),
                "penName", result.penName().value()
        ));
    }

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

    @PreAuthorize("@authorizationService.userOwnsBlog(#blogId, #jwtUserDetails.userId)")
    @GetMapping("/{blogId}/articles/completed")
    public ResponseEntity<?> findCompletedArticles(
            @PathVariable UUID blogId,
            @PageableDefault(sort = "completedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        FindCompletedArticlesArgument argument = new FindCompletedArticlesArgument(BlogId.of(blogId), pageable);
        FindCompletedArticlesResult result = findCompletedArticles.exec(argument);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("@authorizationService.userOwnsBlog(#blogId, #jwtUserDetails.userId)")
    @GetMapping("/{blogId}/articles/draft")
    public ResponseEntity<?> findDraftArticles(
            @PathVariable UUID blogId,
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        FindDraftArticlesArgument argument = new FindDraftArticlesArgument(BlogId.of(blogId), pageable);
        FindDraftArticlesResult result = findDraftArticles.exec(argument);
        return ResponseEntity.ok(result);
    }
}
