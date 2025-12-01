package kr.minimalest.api.infrastructure.persistence.comment;

import kr.minimalest.api.domain.engagement.comment.Comment;
import kr.minimalest.api.domain.engagement.comment.CommentId;
import kr.minimalest.api.domain.engagement.comment.CommentStatus;
import kr.minimalest.api.domain.writing.ArticleId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataJpaCommentRepository extends JpaRepository<Comment, CommentId> {

    /**
     * 아티클의 부모 댓글만 최신순으로 조회
     */
    @Query("""
            SELECT c FROM Comment c
            WHERE c.articleId = :articleId
              AND c.parentId IS NULL
              AND c.status = 'ACTIVE'
            ORDER BY c.createdAt DESC
            """)
    List<Comment> findAllByArticleIdAndParentIdNullOrderByCreatedAtDesc(
            @Param("articleId") ArticleId articleId,
            PageRequest pageRequest
    );

    /**
     * 아티클의 부모 댓글만 인기순으로 조회 (좋아요 많은 순)
     */
    @Query("""
            SELECT c FROM Comment c
            WHERE c.articleId = :articleId
              AND c.parentId IS NULL
              AND c.status = 'ACTIVE'
            ORDER BY c.likeCount DESC, c.createdAt DESC
            """)
    List<Comment> findAllByArticleIdAndParentIdNullOrderByLikeCountDesc(
            @Param("articleId") ArticleId articleId,
            PageRequest pageRequest
    );

    /**
     * 여러 부모 댓글의 대댓글을 한 번에 조회 (최신순)
     * N+1 문제 해결을 위해 IN 쿼리 사용
     */
    @Query("""
            SELECT c FROM Comment c
            WHERE c.parentId IN :parentCommentIds
              AND c.status = 'ACTIVE'
            ORDER BY c.createdAt ASC
            """)
    List<Comment> findAllByParentIdInOrderByCreatedAtAsc(
            @Param("parentCommentIds") List<CommentId> parentCommentIds
    );

    /**
     * 댓글 존재 여부 확인 (ACTIVE 상태만)
     */
    boolean existsByIdAndStatus(CommentId commentId, CommentStatus status);

    /**
     * 좋아요 + 1
     */
    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Comment c
            SET c.likeCount = c.likeCount + 1
            WHERE c.id = :commentId
            """)
    void incrementLikes(CommentId commentId);

    /**
     * 좋아요 - 1
     */
    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Comment c
            SET c.likeCount = c.likeCount - 1
            WHERE c.id = :commentId
            """)
    void decrementLikes(CommentId commentId);
}
