package kr.minimalest.api.web.controller.dto.response;

import java.util.List;

/**
 * 북마크 목록 응답 (컬렉션 정보 + 북마크 목록)
 */
public record BookmarkListResponse(
        int bookmarkCount,
        List<BookmarkResponse> bookmarks
) {
    public static BookmarkListResponse of(int bookmarkCount, List<BookmarkResponse> bookmarks) {
        return new BookmarkListResponse(bookmarkCount, bookmarks);
    }
}
