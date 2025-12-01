package kr.minimalest.api.application.reaction;

import kr.minimalest.api.domain.engagement.reaction.repository.ArticleReactionRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.access.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 내 반응 조회 Use Case
 * 사용자가 특정 아티클에 한 모든 활성 반응을 조회합니다.
 */
@Component
@RequiredArgsConstructor
public class GetMyReactionsFromArticle {

    private final ArticleReactionRepository reactionRepository;

    @Transactional(readOnly = true)
    public GetMyReactionsFromArticleResult exec(GetMyReactionsFromArticleArgument argument) {
        List<MyReactionDetail> reactions = reactionRepository
            .findActiveByArticleIdAndUserId(
                ArticleId.of(argument.articleId()),
                UserId.of(argument.userId())
            )
            .stream()
            .map(MyReactionDetail::of)
            .toList();

        return GetMyReactionsFromArticleResult.of(reactions);
    }
}
