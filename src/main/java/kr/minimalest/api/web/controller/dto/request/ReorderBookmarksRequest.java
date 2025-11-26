package kr.minimalest.api.web.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * 북마크 순서 변경 요청
 */
public record ReorderBookmarksRequest(
        @NotEmpty(message = "북마크 ID 목록은 비워둘 수 없습니다")
        List<@NotNull(message = "북마크 ID는 null일 수 없습니다") UUID> bookmarkIds
) {}
