package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.minimalest.api.application.article.*;
import kr.minimalest.api.application.author.UpdateAuthorProfile;
import kr.minimalest.api.application.author.UpdateAuthorProfileArgument;
import kr.minimalest.api.application.blog.*;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.infrastructure.security.JwtUserDetails;
import kr.minimalest.api.web.controller.dto.request.CreateBlogRequest;
import kr.minimalest.api.web.controller.dto.request.UpdateArticleRequest;
import kr.minimalest.api.web.controller.dto.request.UpdateProfileRequest;
import kr.minimalest.api.web.controller.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blogs")
@Tag(name = "Blog", description = "블로그 및 아티클 관련 API")
public class BlogController {

    private final FindBlog findBlog;
    private final FindBlogSelf findBlogSelf;
    private final CreateBlog createBlog;
    private final UpdateAuthorProfile updateAuthorProfile;
    private final FindProfileImageUrl findProfileImageUrl;

    private final CreateArticle createArticle;
    private final UpdateArticle updateArticle;
    private final PublishArticle publishArticle;
    private final DeleteArticle deleteArticle;

    private final FindSingleArticle findSingleArticle;
    private final FindMyArticles findMyArticles;

    /**
     * 현재 로그인한 사용자의 블로그 정보를 조회합니다.
     * 로그인하지 않으면, 스프링 시큐리티가 401로 응답
     * 로그인 상태지만 블로그가 없으면, 404로 응답
     */
    @GetMapping("self")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "내 블로그 조회", description = "현재 로그인한 사용자의 블로그 정보를 조회합니다. (인증 필수)")
    public ResponseEntity<BlogInfoResponse> findBlogSelf(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        if (jwtUserDetails == null) {
            return ResponseEntity.status(401).build();
        }

        FindBlogSelfArgument argument = new FindBlogSelfArgument(jwtUserDetails.getUserId());
        FindBlogSelfResult result = findBlogSelf.exec(argument);
        return ResponseEntity.ok(BlogInfoResponse.of(result.blogInfo()));
    }

    /**
     * 펜네임을 사용해서 블로그 정보를 조회합니다.
     */
    @GetMapping("{penName}")
    @Operation(summary = "펜네임으로 블로그 조회", description = "펜네임을 사용해 특정 블로그 정보를 조회합니다.")
    public ResponseEntity<BlogInfoResponse> findBlog(
            @PathVariable String penName
    ) {
        FindBlogArgument argument = new FindBlogArgument(penName);
        FindBlogResult result = findBlog.exec(argument);
        return ResponseEntity.ok(BlogInfoResponse.of(result.blogInfo()));
    }

    @GetMapping("{penName}/articles/{articleId}/details")
    @Operation(summary = "아티클 상세 조회", description = "펜네임과 아티클 ID를 사용해 아티클 상세 정보를 조회합니다.")
    public ResponseEntity<ArticleDetailResponse> findArticleDetails(
        @PathVariable("penName") String penNameStr,
        @PathVariable("articleId") UUID articleId
    ) {
        FindSingleArticleArgument argument = new FindSingleArticleArgument(penNameStr, articleId);
        FindSingleArticleResult result = findSingleArticle.exec(argument);

        return ResponseEntity.ok(ArticleDetailResponse.of(result.articleDetail()));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "블로그 생성", description = "새로운 블로그를 생성합니다. (인증 필수, 사용자당 1개 블로그만 가능)")
    public ResponseEntity<BlogIdResponse> createBlog(
            @RequestBody @Valid CreateBlogRequest createBlogRequest,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        CreateBlogArgument argument = CreateBlogArgument.of(jwtUserDetails.getUserId(), createBlogRequest.penName());
        CreateBlogResult result = createBlog.exec(argument);
        return ResponseEntity.ok(BlogIdResponse.of(result.blogId().id()));
    }

    /**
     * 기본 글을 생성합니다.
     */
    @PostMapping("{blogId}/articles")
    @PreAuthorize("isAuthenticated() && @authorizationService.userOwnsBlog(#blogId, #jwtUserDetails.userId)")
    @Operation(summary = "아티클 생성 (초안)", description = "새로운 아티클을 초안 상태로 생성합니다. (인증 필수, 본인의 블로그만 가능)")
    public ResponseEntity<ArticleIdResponse> createArticle(
            @PathVariable UUID blogId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        CreateArticleArgument argument = CreateArticleArgument.of(BlogId.of(blogId));
        CreateArticleResult result = createArticle.exec(argument);
        return ResponseEntity.ok(ArticleIdResponse.of(result.articleId().id()));
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
    @Operation(summary = "아티클 수정", description = "아티클의 제목, 본문, 설명을 수정합니다. (인증 필수, 본인의 아티클만 수정 가능)")
    public ResponseEntity<ArticleIdResponse> updateArticle(
            @PathVariable UUID blogId,
            @PathVariable UUID articleId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @Valid @RequestBody UpdateArticleRequest request
    ) {
        UpdateArticleArgument argument = UpdateArticleArgument.of(
                ArticleId.of(articleId), request.title(), request.content(), request.description()
        );
        updateArticle.exec(argument);
        return ResponseEntity.ok(ArticleIdResponse.of(articleId));
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
    @Operation(summary = "아티클 발행", description = "초안 아티클을 발행 상태로 전환합니다. 유효성 검사(글자 수 등)가 실행됩니다. (인증 필수, 본인의 아티클만 발행 가능)")
    public ResponseEntity<ArticleIdResponse> publishArticle(
            @PathVariable UUID blogId,
            @PathVariable UUID articleId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        PublishArticleArgument argument = PublishArticleArgument.of(ArticleId.of(articleId));
        publishArticle.exec(argument);
        return ResponseEntity.ok(ArticleIdResponse.of(articleId));
    }

    /**
     * 내 글 관리 - 작성중/완성된 글을 통합으로 조회합니다.
     * status 파라미터: DRAFT|COMPLETED|ALL (기본값: ALL)
     * search 파라미터: 제목으로 검색 (선택사항)
     */
    @PreAuthorize("isAuthenticated() && @authorizationService.userOwnsBlog(#blogId, #jwtUserDetails.userId)")
    @GetMapping("/{blogId}/articles/my")
    @Operation(summary = "내 아티클 목록 조회", description = "내 블로그의 아티클 목록을 조회합니다. 상태(DRAFT/COMPLETED/ALL)와 제목 검색으로 필터링 가능합니다. (인증 필수, 본인의 블로그만 조회 가능)")
    public ResponseEntity<ArticleSummaryPageResponse> findMyArticles(
            @PathVariable UUID blogId,
            @RequestParam(value = "status", required = false) String statusParam,
            @RequestParam(value = "search", defaultValue = "") String searchKeyword,
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        FindMyArticlesArgument argument = new FindMyArticlesArgument(blogId, statusParam, searchKeyword, pageable);
        FindMyArticlesResult result = findMyArticles.exec(argument);
        ArticleSummaryPageResponse response = ArticleSummaryPageResponse.of(result.articles());
        return ResponseEntity.ok(response);
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
    @Operation(summary = "아티클 삭제", description = "아티클을 삭제합니다. (소프트 삭제, 인증 필수, 본인의 아티클만 삭제 가능)")
    public ResponseEntity<?> deleteArticle(
            @PathVariable UUID blogId,
            @PathVariable UUID articleId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        deleteArticle.exec(new DeleteArticleArgument(articleId));
        return ResponseEntity.noContent().build();
    }

    /**
     * 프로필 이미지 URL 조회
     */
    @GetMapping("{blogId}/profile")
    @Operation(summary = "프로필 이미지 URL 조회", description = "해당 블로그의 작가 프로필 URL을 조회합니다.")
    public ResponseEntity<FindProfileImageUrlResponse> findProfile(
            @PathVariable UUID blogId
    ) {
        FindProfileImageUrlResult result = findProfileImageUrl.exec(new FindProfileImageUrlArgument(blogId));
        return ResponseEntity.ok(new FindProfileImageUrlResponse(result.profileImageUrl()));
    }

    /**
     * 프로필 이미지 URL 저장
     * S3에 업로드된 후 받은 URL을 저장합니다.
     */
    @PutMapping("{blogId}/profile")
    @PreAuthorize("isAuthenticated() && @authorizationService.userOwnsBlog(#blogId, #jwtUserDetails.userId)")
    @Operation(
            summary = "프로필 이미지 URL 저장",
            description =
                    """
                    S3에 업로드된 프로필 이미지의 URL을 저장합니다. (인증 필수, 본인의 블로그만 수정 가능)
                    """
    )
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @PathVariable UUID blogId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        UpdateAuthorProfileArgument argument = UpdateAuthorProfileArgument.of(
                jwtUserDetails.getUserId().id(),
                request.profileImageUrl()
        );
        var result = updateAuthorProfile.exec(argument);
        return ResponseEntity.ok(UpdateProfileResponse.of(result));
    }
}
