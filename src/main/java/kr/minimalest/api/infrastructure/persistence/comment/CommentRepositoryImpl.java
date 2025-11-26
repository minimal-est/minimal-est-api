package kr.minimalest.api.infrastructure.persistence.comment;

import kr.minimalest.api.domain.engagement.comment.Comment;
import kr.minimalest.api.domain.engagement.comment.CommentId;
import kr.minimalest.api.domain.engagement.comment.CommentStatus;
import kr.minimalest.api.domain.engagement.comment.CommentSortType;
import kr.minimalest.api.domain.engagement.comment.repository.CommentRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final SpringDataJpaCommentRepository springDataJpaRepository;

    @Override
    public CommentId save(Comment comment) {
        Comment savedComment = springDataJpaRepository.save(comment);
        return savedComment.getId();
    }

    @Override
    public Optional<Comment> findById(CommentId commentId) {
        return springDataJpaRepository.findById(commentId);
    }

    @Override
    public List<Comment> findParentComments(
            ArticleId articleId,
            CommentSortType sortType,
            int page,
            int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit);

        return switch (sortType) {
            case LATEST -> springDataJpaRepository.findAllByArticleIdAndParentIdNullOrderByCreatedAtDesc(
                    articleId, pageRequest
            );
            case POPULAR -> springDataJpaRepository.findAllByArticleIdAndParentIdNullOrderByLikeCountDesc(
                    articleId, pageRequest
            );
        };
    }

    @Override
    public List<Comment> findRepliesByParentIds(List<CommentId> parentCommentIds) {
        if (parentCommentIds.isEmpty()) {
            return List.of();
        }
        return springDataJpaRepository.findAllByParentIdInOrderByCreatedAtAsc(parentCommentIds);
    }

    @Override
    public void update(Comment comment) {
        springDataJpaRepository.save(comment);
    }

    @Override
    public boolean existsById(CommentId commentId) {
        return springDataJpaRepository.existsByIdAndStatus(commentId, CommentStatus.ACTIVE);
    }
}
