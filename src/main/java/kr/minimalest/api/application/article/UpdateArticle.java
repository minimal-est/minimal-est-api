package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.discovery.tag.ArticleTag;
import kr.minimalest.api.domain.discovery.tag.Tag;
import kr.minimalest.api.domain.discovery.tag.service.TagService;
import kr.minimalest.api.domain.discovery.tag.repository.ArticleTagRepository;
import kr.minimalest.api.domain.writing.*;
import kr.minimalest.api.domain.writing.exception.ArticleNotFoundException;
import kr.minimalest.api.domain.writing.exception.ArticleStateException;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@RequiredArgsConstructor
public class UpdateArticle {

    private final ArticleRepository articleRepository;
    private final TagService tagService;
    private final ArticleTagRepository articleTagRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void exec(UpdateArticleArgument arg) {
        Article article = findArticle(arg.articleId());
        Title title = Title.of(arg.title());
        Content content = Content.of(arg.content());
        Content pureContent = Content.of(arg.pureContent());
        Description description = new Description(arg.description());

        updateArticleAndPublishEvent(article, title, content, pureContent, description);

        // 태그 저장 (기존 태그 삭제 후 새로 추가)
        if (arg.tags() != null && !arg.tags().isEmpty()) {
            updateTags(article.getId(), arg.tags());
        }
    }

    private void updateArticleAndPublishEvent(Article article, Title title, Content content,
                                              Content pureContent, Description description) {
        try {
            article.update(title, content, pureContent, description);
            publishEvent(article);
        } catch (Exception e) {
            throw new ArticleStateException("글을 수정할 수 없는 상태입니다.", e);
        }
    }

    private void updateTags(ArticleId articleId, List<String> tagNames) {
        // 기존 태그 삭제
        articleTagRepository.deleteByArticleId(articleId);

        // 새로운 태그 생성 또는 조회 (유효하지 않은 태그는 자동 필터링)
        List<Tag> tags = tagService.findOrCreateTags(tagNames);

        // 글에 태그 추가 (중복 체크)
        tags.forEach(tag -> {
            // 이미 있는 경우 스킵, 없는 경우만 추가
            if (articleTagRepository.findByArticleIdAndTagId(articleId, tag.getId()).isEmpty()) {
                articleTagRepository.save(ArticleTag.of(articleId, tag.getId()));
            }
        });
    }

    private Article findArticle(ArticleId articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("해당 ID의 Article을 찾을 수 없습니다."));
    }

    private void publishEvent(Article article) {
        article.releaseEvents().forEach(eventPublisher::publishEvent);
    }
}
