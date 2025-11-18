package kr.minimalest.api.application.reaction;

import kr.minimalest.api.domain.engagement.reaction.ArticleReaction;
import kr.minimalest.api.domain.engagement.reaction.ReactionType;
import kr.minimalest.api.domain.engagement.reaction.repository.ArticleReactionRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.access.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 반응 추가/토글 Use Case
 * 사용자가 반응을 누를 때:
 * - 처음: 반응 생성 (REACTED)
 * - 다시 누르면: 토글 (REACTED ↔ CANCELED)
 * - 다른 반응: 새로운 반응 추가 (기존 반응은 유지)
 */
@Component
@RequiredArgsConstructor
public class AddOrToggleReactionToArticle {

    private final ArticleReactionRepository reactionRepository;

    @Transactional
    public AddOrToggleReactionToArticleResult exec(AddOrToggleReactionToArticleArgument argument) {
        ArticleId articleId = ArticleId.of(argument.articleId());
        UserId userId = UserId.of(argument.userId());
        ReactionType reactionType = ReactionType.valueOf(argument.reactionType());

        // 1. 해당 반응이 이미 있는지 확인
        Optional<ArticleReaction> existingReaction = reactionRepository
            .findByArticleIdAndUserIdAndReactionType(articleId, userId, reactionType);

        ArticleReaction reaction;
        if (existingReaction.isPresent()) {
            // 2a. 이미 있으면: 토글
            reaction = existingReaction.get();
            reaction.toggle();
        } else {
            // 2b. 없으면: 새로 생성 (REACTED 상태)
            reaction = ArticleReaction.create(articleId, userId, reactionType);
        }

        // 3. 저장
        reactionRepository.save(reaction);

        // 4. 반응 ID 반환
        return AddOrToggleReactionToArticleResult.of(reaction.getId().id());
    }
}
