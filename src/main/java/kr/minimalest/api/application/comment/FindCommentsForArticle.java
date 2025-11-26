package kr.minimalest.api.application.comment;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.engagement.comment.Comment;
import kr.minimalest.api.domain.engagement.comment.CommentId;
import kr.minimalest.api.domain.engagement.comment.CommentSortType;
import kr.minimalest.api.domain.engagement.comment.repository.CommentRepository;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Business
@RequiredArgsConstructor
public class FindCommentsForArticle {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    public FindCommentsResult exec(FindCommentsArgument argument) {
        // 1. 부모 댓글만 조회 (정렬, 페이징)
        List<Comment> parentComments = commentRepository.findParentComments(
                argument.articleId(),
                argument.sortType(),
                argument.page(),
                argument.limit()
        );

        // 2. 부모 댓글이 없으면 빈 결과 반환
        if (parentComments.isEmpty()) {
            return FindCommentsResult.of(List.of());
        }

        // 3. 부모 댓글의 ID 추출
        List<CommentId> parentCommentIds = parentComments.stream()
                .map(Comment::getId)
                .toList();

        // 4. 모든 대댓글을 한 번에 조회 (N+1 해결)
        List<Comment> allReplies = commentRepository.findRepliesByParentIds(parentCommentIds);

        // 5. 회원 댓글들의 userId 수집 (부모 + 대댓글)
        Set<UserId> userIds = parentComments.stream()
                .map(Comment::getUserId)
                .filter(Objects::nonNull)  // 비회원 제외
                .collect(Collectors.toSet());

        allReplies.stream()
                .map(Comment::getUserId)
                .filter(Objects::nonNull)  // 비회원 제외
                .forEach(userIds::add);

        // 6. 모든 userId에 해당하는 Blog 정보를 한 번에 조회
        Map<UserId, Blog> blogsByUserId = blogRepository.findByUserIds(new java.util.ArrayList<>(userIds))
                .stream()
                .collect(Collectors.toMap(Blog::getOwnerUserId, Function.identity()));

        // 7. CommentAuthorInfo 생성 함수
        java.util.function.Function<Comment, CommentAuthorInfo> createAuthorInfo = comment -> {
            if (comment.getUserId() == null) {
                return null;  // 비회원
            }
            Blog blog = blogsByUserId.get(comment.getUserId());
            if (blog == null) return null;

            return new CommentAuthorInfo(
                    blog.getPenName().value(),
                    blog.getProfileImageUrl(),
                    "/blog/" + blog.getPenName().value()
            );
        };

        // 8. 대댓글을 부모별로 그룹핑
        Map<CommentId, List<CommentDetail>> repliesByParentId = allReplies.stream()
                .collect(Collectors.groupingBy(
                        Comment::getParentId,
                        Collectors.mapping(
                                reply -> CommentDetail.from(reply, List.of(), createAuthorInfo.apply(reply)),
                                Collectors.toList()
                        )
                ));

        // 9. 부모 댓글과 대댓글을 연결
        List<CommentDetail> comments = parentComments.stream()
                .map(parent -> {
                    List<CommentDetail> replies = repliesByParentId.getOrDefault(
                            parent.getId(),
                            List.of()
                    );
                    return CommentDetail.from(parent, replies, createAuthorInfo.apply(parent));
                })
                .toList();

        return FindCommentsResult.of(comments);
    }
}
