package kr.minimalest.api.domain.engagement.reaction.service;

import kr.minimalest.api.domain.engagement.reaction.ArticleReaction;
import kr.minimalest.api.domain.engagement.reaction.ArticleReactionId;
import kr.minimalest.api.domain.engagement.reaction.ReactionType;
import kr.minimalest.api.domain.engagement.reaction.repository.ArticleReactionRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleReactionServiceImpl implements ArticleReactionService {

    private final ArticleReactionRepository articleReactionRepository;

    @Override
    public void react(ArticleReactionId articleReactionId) {
        Optional<ArticleReaction> reaction = articleReactionRepository.findById(articleReactionId);
        if (reaction.isPresent()) {
            ArticleReaction existingReaction = reaction.get();
            existingReaction.toggle();
            articleReactionRepository.save(existingReaction);
        }
    }

    @Override
    public void removeReaction(ArticleReactionId articleReactionId) {
        Optional<ArticleReaction> reaction = articleReactionRepository.findById(articleReactionId);
        if (reaction.isPresent()) {
            articleReactionRepository.delete(reaction.get());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<ReactionType, Long> getReactionCounts(ArticleId articleId) {
        return articleReactionRepository.countActiveByArticleIdGroupByType(articleId);
    }

    @Override
    public Map<ArticleId, Map<ReactionType, Long>> getReactionCountMappings(List<ArticleId> articleIds) {
        return articleReactionRepository.countActiveByArticleIdsGroupByType(articleIds);
    }
}
