package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.minimalest.api.application.comment.*;
import kr.minimalest.api.infrastructure.security.JwtUserDetails;
import kr.minimalest.api.web.controller.dto.request.CreateCommentRequest;
import kr.minimalest.api.web.controller.dto.request.CreateGuestCommentRequest;
import kr.minimalest.api.web.controller.dto.response.CommentListResponse;
import kr.minimalest.api.web.controller.dto.response.CommentResponse;
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
@RequestMapping("/api/v1/articles/{articleId}/comments")
@Tag(name = "Comment", description = "댓글 관련 API")
public class CommentController {

    private final CreateComment createComment;
    private final CreateGuestComment createGuestComment;
    private final FindCommentsForArticle findCommentsForArticle;
    private final DeleteComment deleteComment;

    /**
     * 댓글 목록 조회 (부모 댓글 + 대댓글)
     */
    @GetMapping
    @Operation(
            summary = "댓글 목록 조회",
            description = "아티클의 댓글과 대댓글을 함께 조회합니다. 정렬 순서는 LATEST(최신순) 또는 POPULAR(인기순)입니다."
    )
    public ResponseEntity<CommentListResponse> findComments(
            @PathVariable UUID articleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "LATEST") String sort
    ) {
        FindCommentsArgument argument = FindCommentsArgument.of(articleId, sort, page, limit);
        FindCommentsResult result = findCommentsForArticle.exec(argument);
        return ResponseEntity.ok(CommentListResponse.of(result));
    }

    /**
     * 회원 댓글/대댓글 작성
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "회원 댓글 작성",
            description = "로그인한 사용자가 댓글 또는 대댓글을 작성합니다. parentCommentId가 있으면 대댓글입니다."
    )
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable UUID articleId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        CreateCommentArgument argument = CreateCommentArgument.ofMember(
                articleId,
                jwtUserDetails.getUserId(),
                null,  // penName은 Use Case에서 조회
                request.content(),
                request.isAnonymous(),
                request.parentCommentId()
        );
        CommentDetail result = createComment.exec(argument);
        return ResponseEntity.ok(CommentResponse.of(result));
    }

    /**
     * 비회원 댓글/대댓글 작성
     */
    @PostMapping("/guest")
    @Operation(
            summary = "비회원 댓글 작성",
            description = "비회원이 댓글 또는 대댓글을 작성합니다. 비밀번호는 삭제 시 필요합니다."
    )
    public ResponseEntity<CommentResponse> createGuestComment(
            @PathVariable UUID articleId,
            @Valid @RequestBody CreateGuestCommentRequest request
    ) {
        // TODO: reCAPTCHA 검증 (나중에)

        CreateGuestCommentArgument argument = CreateGuestCommentArgument.of(
                articleId,
                request.authorName(),
                request.content(),
                request.password(),  // 비밀번호 추가
                request.parentCommentId()
        );
        CommentDetail result = createGuestComment.exec(argument);
        return ResponseEntity.ok(CommentResponse.of(result));
    }

    /**
     * 댓글 삭제 (회원)
     */
    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "댓글 삭제 (회원)",
            description = "자신의 댓글을 삭제합니다. 삭제되면 '삭제된 댓글입니다'로 표시됩니다."
    )
    public ResponseEntity<?> deleteComment(
            @PathVariable UUID articleId,
            @PathVariable UUID commentId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        DeleteCommentArgument argument = DeleteCommentArgument.ofMember(
                commentId,
                jwtUserDetails.getUserId()
        );
        deleteComment.exec(argument);
        return ResponseEntity.noContent().build();
    }

    /**
     * 댓글 삭제 (비회원)
     */
    @DeleteMapping("/{commentId}/guest")
    @Operation(
            summary = "댓글 삭제 (비회원)",
            description = "비회원이 작성한 댓글을 비밀번호로 삭제합니다."
    )
    public ResponseEntity<?> deleteGuestComment(
            @PathVariable UUID articleId,
            @PathVariable UUID commentId,
            @RequestParam String password
    ) {
        DeleteCommentArgument argument = DeleteCommentArgument.ofGuest(
                commentId,
                password
        );
        deleteComment.exec(argument);
        return ResponseEntity.noContent().build();
    }
}
