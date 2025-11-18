package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.reaction.MyReactionDetail;

import java.util.List;

public record MyReactionResponse(
        List<MyReactionDetail> reactions
) {
    public static MyReactionResponse of(List<MyReactionDetail> reactions) {
        return new MyReactionResponse(reactions);
    }
}
