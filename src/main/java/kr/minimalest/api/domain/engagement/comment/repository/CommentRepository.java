package kr.minimalest.api.domain.engagement.comment.repository;

import kr.minimalest.api.domain.engagement.comment.Comment;
import kr.minimalest.api.domain.engagement.comment.CommentId;
import kr.minimalest.api.domain.engagement.comment.CommentSortType;
import kr.minimalest.api.domain.writing.ArticleId;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    /**
     * 댓글 저장
     */
    CommentId save(Comment comment);

    /**
     * ID로 댓글 조회
     */
    Optional<Comment> findById(CommentId commentId);

    /**
     * 아티클의 부모 댓글만 조회 (정렬, 페이지 지원)
     * @param articleId 아티클 ID
     * @param sortType 정렬 타입 (LATEST, POPULAR)
     * @param page 페이지 (0부터 시작)
     * @param limit 한 페이지의 댓글 수
     */
    List<Comment> findParentComments(
            ArticleId articleId,
            CommentSortType sortType,
            int page,
            int limit
    );

    /**
     * 여러 부모 댓글의 대댓글을 한 번에 조회 (N+1 해결)
     * @param parentCommentIds 부모 댓글 ID 리스트
     */
    List<Comment> findRepliesByParentIds(List<CommentId> parentCommentIds);

    /**
     * 댓글 업데이트
     */
    void update(Comment comment);

    /**
     * 부모 댓글이 존재하는지 확인
     */
    boolean existsById(CommentId commentId);

    // 좋아요 + 1
    void incrementLikes(CommentId comment);

    // 좋아요 - 1
    void decrementLikes(CommentId comment);
}
