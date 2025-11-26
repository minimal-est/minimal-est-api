package kr.minimalest.api.application.bookmark;

import java.util.List;

/**
 * 컬렉션의 북마크 목록 상세 정보
 */
public record CollectionBookmarksDetail(
        int bookmarkCount,
        List<BookmarkDetail> bookmarks
) {
    public static CollectionBookmarksDetail of(int bookmarkCount, List<BookmarkDetail> bookmarks) {
        return new CollectionBookmarksDetail(bookmarkCount, bookmarks);
    }
}
