package kr.minimalest.api.web.controller.dto.response;

import java.util.UUID;

public record BlogInfoResponse(
        UUID blogId,
        UUID userId,
        String penName
) { }
