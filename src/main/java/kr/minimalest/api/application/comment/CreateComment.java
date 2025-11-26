package kr.minimalest.api.application.comment;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.engagement.comment.Comment;
import kr.minimalest.api.domain.engagement.comment.exception.CannotReplyToReplyException;
import kr.minimalest.api.domain.engagement.comment.exception.CommentNotFoundException;
import kr.minimalest.api.domain.engagement.comment.repository.CommentRepository;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class CreateComment {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final BlogRepository blogRepository;

    @Transactional
    public CommentDetail exec(CreateCommentArgument argument) {
        // 1. 아티클 존재 확인
        articleRepository.findById(argument.articleId())
                .orElseThrow(() -> new IllegalArgumentException("아티클을 찾을 수 없습니다"));

        // 2. 대댓글인 경우 부모 댓글 확인 (2단계 제약)
        if (argument.parentCommentId() != null) {
            Comment parentComment = commentRepository.findById(argument.parentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException("부모 댓글을 찾을 수 없습니다"));

            // 대댓글에는 댓글을 달 수 없음 (2단계 제약)
            if (parentComment.isReply()) {
                throw new CannotReplyToReplyException("대댓글에는 댓글을 달 수 없습니다");
            }
        }

        // 3. 작성자 정보 조회 (펜네임)
        String penName = blogRepository.findAuthorByUserId(argument.userId())
                .map(author -> author.getPenName().value())
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다"));

        // 4. 댓글 생성
        Comment comment = Comment.createByMember(
                argument.articleId(),
                argument.parentCommentId(),
                argument.userId(),
                penName,
                argument.isAnonymous(),
                argument.content()
        );

        commentRepository.save(comment);
        return CommentDetail.from(comment, null);
    }
}
