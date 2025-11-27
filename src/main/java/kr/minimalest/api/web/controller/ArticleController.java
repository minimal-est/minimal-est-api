package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.minimalest.api.application.article.*;
import kr.minimalest.api.application.reaction.AddOrToggleReactionToArticle;
import kr.minimalest.api.application.reaction.AddOrToggleReactionToArticleArgument;
import kr.minimalest.api.application.reaction.GetArticleReactionStats;
import kr.minimalest.api.application.reaction.GetArticleReactionStatsArgument;
import kr.minimalest.api.application.reaction.GetMyReactionsFromArticle;
import kr.minimalest.api.application.reaction.GetMyReactionsFromArticleArgument;
import kr.minimalest.api.infrastructure.security.JwtUserDetails;
import kr.minimalest.api.web.controller.dto.request.AddReactionRequest;
import kr.minimalest.api.web.controller.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
@Tag(name = "Article", description = "아티클 추천 및 상호작용 관련 API")
public class ArticleController {

    private final FindRecommendArticles findRecommendArticles;
    private final FindPrevAndNextArticle findPrevAndNextArticle;
    private final AddOrToggleReactionToArticle addOrToggleReactionToArticle;
    private final GetArticleReactionStats getArticleReactionStats;
    private final GetMyReactionsFromArticle getMyReactionsFromArticle;

    @GetMapping("/recommend")
    @Operation(summary = "추천 아티클 조회", description = "추천 알고리즘에 따른 아티클 목록을 조회합니다. (페이지, 제한 갯수 지정 가능)")
    public ResponseEntity<ArticleSummaryListResponse> findArticles(
            // 추천 알고리즘에 따라 변경될 수 있음
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit
    ) {
        if (page == null) page = 0;
        if (limit == null) limit = 5;
        FindRecommendArticlesArgument argument = FindRecommendArticlesArgument.limitOf(page, limit);
        FindRecommendArticlesResult result = findRecommendArticles.exec(argument);
        ArticleSummaryListResponse response = ArticleSummaryListResponse.of(result.articleSummaries());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{articleId}/prev-and-next")
    @Operation(summary = "해당 아티클 블로그의 이전, 다음 글 조회", description = "발행일자 기준으로 해당 아티클 블로그의 이전 글과 다음 글을 조회합니다. (없으면 " +
            "null로 반환)")
    public ResponseEntity<ArticleSummaryOfPrevAndNextResponse> findArticleOfPrevAndNext(
            @PathVariable UUID articleId
    ) {
        FindPrevAndNextArticleArgument argument = new FindPrevAndNextArticleArgument(articleId);
        FindPrevAndNextArticleResult result = findPrevAndNextArticle.exec(argument);
        ArticleSummaryResponse prevResp = result.prevArticle() == null ? null :
                ArticleSummaryResponse.of(result.prevArticle());
        ArticleSummaryResponse nextResp = result.nextArticle() == null ? null :
                ArticleSummaryResponse.of(result.nextArticle());
        return ResponseEntity.ok(new ArticleSummaryOfPrevAndNextResponse(prevResp, nextResp));
    }

    @PostMapping("/{articleId}/reactions")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "반응 추가/토글",
            description = "아티클에 반응을 추가합니다. 이미 같은 반응이 있으면 '토글'됩니다. 반응은 여러 개가 동시에 존재할 수 있습니다."
    )
    public ResponseEntity<ReactionIdResponse> addOrToggleReaction(
            @PathVariable UUID articleId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @RequestBody @Valid AddReactionRequest request
    ) {
        AddOrToggleReactionToArticleArgument argument = AddOrToggleReactionToArticleArgument.of(
                articleId,
                jwtUserDetails.getUserId().id(),
                request.reactionType()
        );
        var result = addOrToggleReactionToArticle.exec(argument);
        return ResponseEntity.ok(ReactionIdResponse.of(result.reactionId()));
    }

    @GetMapping("/{articleId}/reactions/stats")
    @Operation(
            summary = "반응 통계 조회",
            description = "아티클의 전체 반응 통계를 반응 타입별로 조회합니다. (READ: 10, AGREE: 5..)"
    )
    public ResponseEntity<ReactionStatsResponse> getReactionStats(
            @PathVariable UUID articleId
    ) {
        GetArticleReactionStatsArgument argument = GetArticleReactionStatsArgument.of(articleId);
        var result = getArticleReactionStats.exec(argument);
        return ResponseEntity.ok(ReactionStatsResponse.of(result.stats()));
    }

    @GetMapping("/{articleId}/reactions/self")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "내 반응 조회",
            description = "로그인한 사용자가 해당 아티클에 한 모든 활성 반응을 조회합니다."
    )
    public ResponseEntity<MyReactionResponse> getMyReactions(
            @PathVariable UUID articleId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        GetMyReactionsFromArticleArgument argument = GetMyReactionsFromArticleArgument.of(
                articleId,
                jwtUserDetails.getUserId().id()
        );
        var result = getMyReactionsFromArticle.exec(argument);
        return ResponseEntity.ok(MyReactionResponse.of(result.reactions()));
    }

}
