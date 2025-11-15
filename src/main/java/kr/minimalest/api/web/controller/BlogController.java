package kr.minimalest.api.web.controller;

import jakarta.validation.Valid;
import kr.minimalest.api.application.article.*;
import kr.minimalest.api.application.blog.*;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.ArticleStatus;
import kr.minimalest.api.infrastructure.security.JwtUserDetails;
import kr.minimalest.api.web.controller.dto.request.CreateBlogRequest;
import kr.minimalest.api.web.controller.dto.request.UpdateArticleRequest;
import kr.minimalest.api.web.controller.dto.response.ArticleDetailResponse;
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
    private final PublishArticle publishArticle;
    private final DeleteArticle deleteArticle;

    private final FindSingleArticle findSingleArticle;
    private final FindDraftArticles findDraftArticles;
    private final FindCompletedArticles findCompletedArticles;
    private final FindMyArticles findMyArticles;

    /**
     * 현재 로그인한 사용자의 블로그 정보를 조회합니다.
     */
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

    /**
     * 펜네임을 사용해서 블로그 정보를 조회합니다.
     */
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

    @GetMapping("{penName}/articles/{articleId}/details")
    public ResponseEntity<?> findArticleDetails(
        @PathVariable("penName") String penNameStr,
        @PathVariable("articleId") UUID articleId
    ) {
        FindSingleArticleArgument argument = new FindSingleArticleArgument(penNameStr, articleId);
        FindSingleArticleResult result = findSingleArticle.exec(argument);

        return ResponseEntity.ok(ArticleDetailResponse.of(result.articleDetail()));
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

    /**
     * 기본 글을 생성합니다.
     */
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

    /**
     * 글을 업데이트 시킵니다. 발행상태에서의 수정은 유효성을 검사하지만, 그 외에는 검사하지 않습니다.
     */
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
                ArticleId.of(articleId), request.title(), request.content(), request.description()
        );
        updateArticle.exec(argument);
        return ResponseEntity.ok(
                Map.of(
                        "articleId", articleId,
                        "title", request.title()
                ));
    }

    /**
     * 글을 완성(발행) 시킵니다. 실질적인 글의 유효성(글자 수 등)을 검사하는 엔드포인트입니다.
     */
    @PostMapping("{blogId}/articles/{articleId}/publish")
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
        PublishArticleArgument argument = PublishArticleArgument.of(ArticleId.of(articleId));
        publishArticle.exec(argument);
        return ResponseEntity.ok(
                Map.of("articleId", articleId)
        );
    }

    /**
     * 내 글 관리 - 작성중/완성된 글을 통합으로 조회합니다.
     * status 파라미터: DRAFT|COMPLETED|ALL (기본값: ALL)
     * search 파라미터: 제목으로 검색 (선택사항)
     */
    @PreAuthorize("@authorizationService.userOwnsBlog(#blogId, #jwtUserDetails.userId)")
    @GetMapping("/{blogId}/articles/my")
    public ResponseEntity<?> findMyArticles(
            @PathVariable UUID blogId,
            @RequestParam(value = "status", required = false) String statusParam,
            @RequestParam(value = "search", defaultValue = "") String searchKeyword,
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        ArticleStatus status = statusParam != null && !statusParam.isEmpty()
                ? ArticleStatus.valueOf(statusParam.toUpperCase())
                : null;

        FindMyArticlesArgument argument = new FindMyArticlesArgument(blogId, status, searchKeyword, pageable);
        FindMyArticlesResult result = findMyArticles.exec(argument);

        return ResponseEntity.ok(result);
    }

    /**
     * 글 삭제 (소프트 삭제)
     */
    @PreAuthorize(
            """
            @authorizationService.userOwnsBlog(#blogId, #jwtUserDetails.userId) and
            @authorizationService.blogOwnsArticle(#blogId, #articleId)
            """
    )
    @DeleteMapping("/{blogId}/articles/{articleId}")
    public ResponseEntity<?> deleteArticle(
            @PathVariable UUID blogId,
            @PathVariable UUID articleId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        deleteArticle.exec(new DeleteArticleArgument(articleId));
        return ResponseEntity.noContent().build();
    }
}
