package kr.minimalest.api.web.controller.dto.response;

import java.util.UUID;

public record ReactionIdResponse(UUID reactionId) {
    public static ReactionIdResponse of(UUID reactionId) {
        return new ReactionIdResponse(reactionId);
    }
}
