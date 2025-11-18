package kr.minimalest.api.application.reaction;

import kr.minimalest.api.application.reaction.MyReactionDetail;

import java.util.List;

/**
 * 내 반응 조회 응답
 */
public record GetMyReactionsFromArticleResult(
    List<MyReactionDetail> reactions
) {
    public static GetMyReactionsFromArticleResult of(List<MyReactionDetail> reactions) {
        return new GetMyReactionsFromArticleResult(reactions);
    }
}
