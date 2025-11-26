package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.minimalest.api.application.bookmark.*;
import kr.minimalest.api.infrastructure.security.JwtUserDetails;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkId;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.web.controller.dto.request.CreateBookmarkCollectionRequest;
import kr.minimalest.api.web.controller.dto.request.ReorderBookmarksRequest;
import kr.minimalest.api.web.controller.dto.request.UpdateBookmarkCollectionRequest;
import kr.minimalest.api.web.controller.dto.response.BookmarkCollectionResponse;
import kr.minimalest.api.web.controller.dto.response.BookmarkListResponse;
import kr.minimalest.api.web.controller.dto.response.BookmarkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Bookmarks", description = "북마크 컬렉션 및 북마크 관리 API")
@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final CreateBookmarkCollection createBookmarkCollection;
    private final AddBookmark addBookmark;
    private final RemoveBookmark removeBookmark;
    private final MoveBookmark moveBookmark;
    private final ReorderBookmarks reorderBookmarks;
    private final FindUserBookmarkCollections findUserBookmarkCollections;
    private final FindCollectionBookmarks findCollectionBookmarks;
    private final UpdateBookmarkCollection updateBookmarkCollection;
    private final DeleteBookmarkCollection deleteBookmarkCollection;
    private final ToggleCollectionPublic toggleCollectionPublic;
    private final FindPublicBookmarkCollection findPublicBookmarkCollection;

    /**
     * 북마크 컬렉션 생성
     */
    @Operation(summary = "북마크 컬렉션 생성", description = "새로운 북마크 컬렉션(폴더)을 생성합니다")
    @PostMapping("/collections")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookmarkCollectionResponse> createCollection(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @Valid @RequestBody CreateBookmarkCollectionRequest request
    ) {
        CreateBookmarkCollection.CreateBookmarkCollectionArgument argument =
                new CreateBookmarkCollection.CreateBookmarkCollectionArgument(
                jwtUserDetails.getUserId(),
                request.title(),
                request.description()
        );
        BookmarkCollectionDetail result = createBookmarkCollection.exec(argument);
        return ResponseEntity.status(HttpStatus.CREATED).body(BookmarkCollectionResponse.of(result));
    }

    /**
     * 사용자의 모든 컬렉션 조회
     */
    @Operation(summary = "사용자 컬렉션 목록 조회", description = "로그인한 사용자의 모든 북마크 컬렉션을 조회합니다")
    @GetMapping("/collections")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookmarkCollectionResponse>> getCollections(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        List<BookmarkCollectionDetail> results = findUserBookmarkCollections.exec(jwtUserDetails.getUserId());
        List<BookmarkCollectionResponse> responses = results.stream()
                .map(BookmarkCollectionResponse::of)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * 컬렉션 정보 조회 (공개 컬렉션만)
     */
    @Operation(summary = "컬렉션 정보 조회", description = "공개된 북마크 컬렉션의 정보를 조회합니다 (인증 불필요)")
    @GetMapping("/collections/{collectionId}")
    public ResponseEntity<BookmarkCollectionResponse> getCollection(
            @PathVariable UUID collectionId
    ) {
        BookmarkCollectionDetail result = findPublicBookmarkCollection.exec(collectionId);
        return ResponseEntity.ok(BookmarkCollectionResponse.of(result));
    }

    /**
     * 컬렉션 정보 업데이트
     */
    @Operation(summary = "컬렉션 정보 업데이트", description = "북마크 컬렉션의 제목과 설명을 수정합니다")
    @PatchMapping("/collections/{collectionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookmarkCollectionResponse> updateCollection(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @PathVariable UUID collectionId,
            @Valid @RequestBody UpdateBookmarkCollectionRequest request
    ) {
        UpdateBookmarkCollection.UpdateBookmarkCollectionArgument argument =
                new UpdateBookmarkCollection.UpdateBookmarkCollectionArgument(
                jwtUserDetails.getUserId(),
                BookmarkCollectionId.of(collectionId),
                request.title(),
                request.description()
        );
        BookmarkCollectionDetail result = updateBookmarkCollection.exec(argument);
        return ResponseEntity.ok(BookmarkCollectionResponse.of(result));
    }

    /**
     * 컬렉션 공개/비공개 토글
     */
    @Operation(summary = "컬렉션 공개 여부 토글", description = "북마크 컬렉션의 공개/비공개 상태를 전환합니다")
    @PatchMapping("/collections/{collectionId}/toggle-public")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookmarkCollectionResponse> togglePublic(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @PathVariable UUID collectionId
    ) {
        ToggleCollectionPublic.ToggleCollectionPublicArgument argument =
                new ToggleCollectionPublic.ToggleCollectionPublicArgument(
                jwtUserDetails.getUserId(),
                BookmarkCollectionId.of(collectionId)
        );
        BookmarkCollectionDetail result = toggleCollectionPublic.exec(argument);
        return ResponseEntity.ok(BookmarkCollectionResponse.of(result));
    }

    /**
     * 컬렉션 삭제
     */
    @Operation(summary = "컬렉션 삭제", description = "북마크 컬렉션을 삭제하고 포함된 북마크를 기본 컬렉션으로 이동합니다")
    @DeleteMapping("/collections/{collectionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteCollection(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @PathVariable UUID collectionId
    ) {
        DeleteBookmarkCollection.DeleteBookmarkCollectionArgument argument =
                new DeleteBookmarkCollection.DeleteBookmarkCollectionArgument(
                jwtUserDetails.getUserId(),
                BookmarkCollectionId.of(collectionId)
        );
        deleteBookmarkCollection.exec(argument);
        return ResponseEntity.noContent().build();
    }

    /**
     * 글 저장 (북마크 추가)
     */
    @Operation(summary = "글을 컬렉션에 저장", description = "특정 글을 북마크 컬렉션에 저장합니다")
    @PostMapping("/collections/{collectionId}/articles/{articleId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookmarkResponse> addBookmark(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @PathVariable UUID collectionId,
            @PathVariable UUID articleId
    ) {
        AddBookmark.AddBookmarkArgument argument = new AddBookmark.AddBookmarkArgument(
                jwtUserDetails.getUserId(),
                BookmarkCollectionId.of(collectionId),
                ArticleId.of(articleId)
        );
        BookmarkDetail result = addBookmark.exec(argument);
        return ResponseEntity.status(HttpStatus.CREATED).body(BookmarkResponse.of(result));
    }

    /**
     * 컬렉션의 북마크 목록 조회
     */
    @Operation(summary = "컬렉션의 북마크 목록 조회", description = "특정 컬렉션에 저장된 모든 북마크를 순서대로 조회합니다")
    @GetMapping("/collections/{collectionId}/bookmarks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookmarkListResponse> getCollectionBookmarks(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @PathVariable UUID collectionId
    ) {
        FindCollectionBookmarks.FindCollectionBookmarksArgument argument =
                new FindCollectionBookmarks.FindCollectionBookmarksArgument(
                jwtUserDetails.getUserId(),
                BookmarkCollectionId.of(collectionId)
        );
        CollectionBookmarksDetail result = findCollectionBookmarks.exec(argument);
        List<BookmarkResponse> bookmarkResponses = result.bookmarks().stream()
                .map(BookmarkResponse::of)
                .toList();
        BookmarkListResponse response = BookmarkListResponse.of(result.bookmarkCount(), bookmarkResponses);
        return ResponseEntity.ok(response);
    }

    /**
     * 북마크 제거
     */
    @Operation(summary = "북마크 제거", description = "저장된 북마크를 삭제합니다")
    @DeleteMapping("/{bookmarkId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeBookmark(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @PathVariable UUID bookmarkId
    ) {
        RemoveBookmark.RemoveBookmarkArgument argument = new RemoveBookmark.RemoveBookmarkArgument(
                jwtUserDetails.getUserId(),
                BookmarkId.of(bookmarkId)
        );
        removeBookmark.exec(argument);
        return ResponseEntity.noContent().build();
    }

    /**
     * 북마크 이동
     */
    @Operation(summary = "북마크 다른 컬렉션으로 이동", description = "저장된 북마크를 다른 컬렉션으로 이동합니다")
    @PatchMapping("/{bookmarkId}/move")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookmarkResponse> moveBookmark(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @PathVariable UUID bookmarkId,
            @RequestParam UUID toCollectionId
    ) {
        MoveBookmark.MoveBookmarkArgument argument = new MoveBookmark.MoveBookmarkArgument(
                jwtUserDetails.getUserId(),
                BookmarkId.of(bookmarkId),
                BookmarkCollectionId.of(toCollectionId)
        );
        BookmarkDetail result = moveBookmark.exec(argument);
        return ResponseEntity.ok(BookmarkResponse.of(result));
    }

    /**
     * 북마크 순서 변경 (드래그 정렬)
     */
    @Operation(summary = "북마크 순서 변경", description = "컬렉션의 북마크들을 드래그해서 순서를 변경합니다")
    @PatchMapping("/collections/{collectionId}/reorder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> reorderBookmarks(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @PathVariable UUID collectionId,
            @Valid @RequestBody ReorderBookmarksRequest request
    ) {
        List<BookmarkId> bookmarkIds = request.bookmarkIds().stream()
                .map(BookmarkId::of)
                .toList();
        ReorderBookmarks.ReorderBookmarksArgument argument =
                new ReorderBookmarks.ReorderBookmarksArgument(
                jwtUserDetails.getUserId(),
                BookmarkCollectionId.of(collectionId),
                bookmarkIds
        );
        reorderBookmarks.exec(argument);
        return ResponseEntity.noContent().build();
    }
}
