package kr.minimalest.api.web.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 북마크 컬렉션 생성 요청
 */
public record CreateBookmarkCollectionRequest(
        @NotBlank(message = "컬렉션 제목은 비워둘 수 없습니다")
        @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다")
        String title,

        @Size(max = 5000, message = "설명은 5000자 이하여야 합니다")
        String description
) {}
